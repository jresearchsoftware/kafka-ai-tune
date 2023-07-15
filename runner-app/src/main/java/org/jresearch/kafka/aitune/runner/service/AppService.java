package org.jresearch.kafka.aitune.runner.service;

import java.util.Iterator;

import org.apache.commons.lang3.RandomStringUtils;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.runner.app.conf.RunnerConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService implements ApplicationRunner {

	@Autowired
	private ConfigurableApplicationContext appContext;

	@Autowired
	@NonNull
	private RunnerConfigurations runnerConfigurations;

	@Autowired
	@NonNull
	private AdminService adminService;

	@Autowired
	@NonNull
	private RunnerService runnerService;

	@Autowired
	@NonNull
	final AppConfig config;

	@Autowired
	@NonNull
	private MetricService metricService;

	private final String experimentId = RandomStringUtils.randomAlphanumeric(5);

	private  Iterator<RunnerConfig> iterator;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Start running app");
		iterator = runnerConfigurations.getRunners().iterator();

		if (!adminService.topicExists(config.getAdminReqTopic())) {
			adminService.createTopic(config.getAdminReqTopic(), 1, 1);
		}
		if (!adminService.topicExists(config.getAdminResTopic())) {
			adminService.createTopic(config.getAdminResTopic(), 1, 1);
		}
		log.info("Starting experiment id: {}", experimentId);

		metricService.startExperiment(experimentId);
		send();
	}

	private void send() {
		if (iterator.hasNext()) {
			RunnerConfig r = iterator.next();
			if (r.isShouldCreateTopic()) {
				log.debug("Creating topic {}", r.getTopic());
				adminService.createTopic(r);
			}
			log.info("Sending load for topic {}", r.getTopic());
			runnerService.send(config.getAdminReqTopic(), experimentId, r);
		} else {
			appContext.close();
		}
	}
	
	@KafkaListener(topics = "#{config.adminResTopic}", groupId = "#{config.runnerConsumerGroup}")
	public void consume(@Payload String s,  @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String experimentId) {
		log.info("Received finishing of loading for topic {}", s);
		send();
	}
}
