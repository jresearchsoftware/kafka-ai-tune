package org.jresearch.kafka.aitune.runner.service;

import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RunnerService {

	@Autowired
	@NonNull
	protected final KafkaTemplate<String, RunnerConfig> kafkaTemplate;
	
	public void send(String adminTopic, String experimentId, RunnerConfig conf) {
		kafkaTemplate.send(adminTopic, experimentId, conf);
	}
}
