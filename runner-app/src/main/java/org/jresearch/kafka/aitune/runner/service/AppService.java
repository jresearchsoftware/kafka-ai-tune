package org.jresearch.kafka.aitune.runner.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.jresearch.kafka.aitune.runner.app.conf.AppConfig;
import org.jresearch.kafka.aitune.runner.app.conf.RunnerConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

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
	private AdminService adminService;

	@Autowired
	@NonNull
	private RunnerService runnerService;

	@Autowired
	@NonNull
	private AppConfig config;

	@Autowired
	@NonNull
	private MetricService metricService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Start running app");
		if (!adminService.topicExists(config.getAdminTopicName())) {
			adminService.createTopic(config.getAdminTopicName(), 1, 1);
		}
		String experimentId = RandomStringUtils.randomAlphanumeric(5);
		log.info("Starting experiment id: {}", experimentId);

		metricService.startExperiment(experimentId);
		runnerConfigurations.getRunners().stream().forEach(r -> {
			if (r.isShouldCreateTopic()) {
				log.debug("Creating topic {}", r.getTopic());
				adminService.createTopic(r);
			}
			runnerService.send(config.getAdminTopicName(), experimentId, r);
		});
	}

}
