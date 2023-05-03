package org.jresearch.kafka.aitune.app.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.jresearch.kafka.aitune.app.conf.RunnerConfigurations;
import org.jresearch.kafka.aitune.runner.content.ContentProvider;
import org.jresearch.kafka.aitune.runner.service.ContentProviderService;
import org.jresearch.kafka.aitune.runner.service.KafkaListenerService;
import org.jresearch.kafka.aitune.runner.service.KafkaTemplateService;
import org.jresearch.kafka.aitune.runner.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AppService implements ApplicationRunner {

	@Autowired
	private RunnerConfigurations runnerConfigurations;

	@Autowired
	private KafkaTemplateService kafkaTemplateService;

	@Autowired
	private KafkaListenerService kafkaListenerService;

	@Autowired
	private ContentProviderService contentProviderService;
	
	@Autowired
	private MeterRegistry registry;
	

	@Override
	public void run(ApplicationArguments args) throws Exception {
		AtomicLong atl = new AtomicLong();
		registry.more().timeGauge("run_start", null,
				atl, TimeUnit.SECONDS, AtomicLong::doubleValue);
		atl.set(System.currentTimeMillis() / 1000);
		runnerConfigurations.getRunners().stream().forEach(r -> {
			KafkaTemplate template = kafkaTemplateService.getTemplate(r);
			ContentProvider keyProvier = contentProviderService.getContentProvider(r.getWorkloadConfig(), true);
			ContentProvider valueProvier = contentProviderService.getContentProvider(r.getWorkloadConfig(), false);
			ProducerService producerService = new ProducerService<>(r, template, keyProvier, valueProvier, registry);
			producerService.run();
			KafkaMessageListenerContainer container = kafkaListenerService.getListener(r);
			container.start();
		});
	}
}
