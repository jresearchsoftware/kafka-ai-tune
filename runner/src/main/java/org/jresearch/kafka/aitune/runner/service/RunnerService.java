package org.jresearch.kafka.aitune.runner.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class RunnerService {

	@Autowired
	@NonNull
	protected final KafkaTemplate<String, RunnerConfig> kafkaTemplate;

	public void send(String adminTopic, RunnerConfig conf) {
		String experimentId = RandomStringUtils.randomAlphanumeric(5);
		kafkaTemplate.send(adminTopic, experimentId, conf);
	}
}
