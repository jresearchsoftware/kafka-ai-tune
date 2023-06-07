package org.jresearch.kafka.aitune.runner.app.service;

import org.jresearch.kafka.aitune.runner.app.conf.AppConfig;
import org.jresearch.kafka.aitune.runner.app.conf.RunnerConfigurations;
import org.jresearch.kafka.aitune.runner.service.AdminService;
import org.jresearch.kafka.aitune.runner.service.RunnerService;
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

//	@Autowired
//	@NonNull
//	private MeterRegistry registry;

	@Autowired
	@NonNull
	private AdminService adminService;

	@Autowired
	@NonNull
	private RunnerService runnerService;

	@Autowired
	@NonNull
	private AppConfig config;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Start running app");
		if(!adminService.topicExists(config.getAdminTopicName())) {
			adminService.createTopic(config.getAdminTopicName(),1,1);
		}
//		AtomicLong atl = new AtomicLong();
//		registry.more().timeGauge("run_start", null, atl, TimeUnit.SECONDS, AtomicLong::doubleValue);
//		atl.set(System.currentTimeMillis() / 1000);
		runnerConfigurations.getRunners().stream().forEach(r -> {
			// r.setTopic(r.getTopic() + "_" + experimentId);
			if (r.isShouldCreateTopic()) {
				log.debug("Creating topic {}", r.getTopic());
				adminService.createTopic(r);
			}
			runnerService.send(config.getAdminTopicName(),r);
		});
	}

}
