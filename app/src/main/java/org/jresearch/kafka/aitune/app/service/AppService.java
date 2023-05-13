package org.jresearch.kafka.aitune.app.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.RandomStringUtils;
import org.jresearch.kafka.aitune.app.conf.RunnerConfigurations;
import org.jresearch.kafka.aitune.runner.content.ContentProvider;
import org.jresearch.kafka.aitune.runner.service.AdminService;
import org.jresearch.kafka.aitune.runner.service.ContentProviderService;
import org.jresearch.kafka.aitune.runner.service.KafkaListenerService;
import org.jresearch.kafka.aitune.runner.service.KafkaTemplateService;
import org.jresearch.kafka.aitune.runner.service.ProducerService;
import org.jresearch.kafka.aitune.runner.service.RunnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService implements ApplicationRunner {

	@Autowired
	@NonNull
	private RunnerConfigurations runnerConfigurations;

	@Autowired
	@NonNull
	private KafkaTemplateService kafkaTemplateService;

	@Autowired
	@NonNull
	private KafkaListenerService kafkaListenerService;

	@Autowired
	@NonNull
	private ContentProviderService contentProviderService;

	@Autowired
	@NonNull
	private MeterRegistry registry;

	@Autowired
	@NonNull
	private AdminService adminService;
	
	@Value("${wait.consumers.delay.ms:2000}")
	private long waitForConsumerDelay;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Start running app");
		AtomicLong atl = new AtomicLong();
		registry.more().timeGauge("run_start", null, atl, TimeUnit.SECONDS, AtomicLong::doubleValue);
		atl.set(System.currentTimeMillis() / 1000);
		String experimentId = RandomStringUtils.randomAlphanumeric(5);
		runnerConfigurations.getRunners().stream().forEach(r -> {
			//r.setTopic(r.getTopic() + "_" + experimentId);
			if (r.isShouldCreateTopic()) {
				log.debug("Creating topic {}",r.getTopic());
				adminService.createTopic(r);
			}

			if (r.getConsumerName() != null) {
				log.info("Starting consumer service");
				KafkaMessageListenerContainer container = kafkaListenerService.getListener(r);
				container.start();
			}
			while (!adminService.consumersReady(r.getWaitForConsumerGroups())) {
				try {
					Thread.sleep(waitForConsumerDelay);
					log.info("Consumers are not ready ...");
				} catch (InterruptedException e) {
					throw new RunnerException("Error while waiting for consumers", e);
				}
			}
			if (r.getProducerName() != null) {
				log.info("Starting producer service");
				KafkaTemplate template = kafkaTemplateService.getTemplate(r);
				ContentProvider keyProvier = contentProviderService.getKeyContentProvider(r.getWorkloadConfig());
				ContentProvider valueProvier = contentProviderService.getValueContentProvider(r.getWorkloadConfig());
				ProducerService producerService = new ProducerService<>(r, template, keyProvier, valueProvier,
						registry);
				producerService.run();
			}

		});
	}

}
