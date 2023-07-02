package org.jresearch.kafka.aitune.client.service;

import java.time.Instant;

import org.jresearch.kafka.aitune.client.conf.NameUtil;
import org.jresearch.kafka.aitune.client.model.ClientExperiment;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricService {
	
	private final MetricRepository metricRepo;
	
	public void startExperiment(String experimentId, String clientId) {
		metricRepo.save(new ClientExperiment(clientId,Instant.now(),experimentId));
	}
	
}