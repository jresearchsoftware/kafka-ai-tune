package org.jresearch.kafka.aitune.runner.service;

import java.time.Instant;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricService {
	
	private final MetricRepository metricRepo;
	
	public void startExperiment(String experimentId) {
		metricRepo.save(new Experiment(experimentId,Instant.now()));
	}
	
}
