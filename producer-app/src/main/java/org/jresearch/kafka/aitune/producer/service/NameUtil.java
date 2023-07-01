package org.jresearch.kafka.aitune.producer.service;

import org.jresearch.kafka.aitune.client.model.RunnerConfig;

public class NameUtil {

	public static final String getClientId(String experimentId, RunnerConfig runnerConfig) {
		return String.join("_", experimentId, runnerConfig.getTopic(), runnerConfig.getProducerName());

	}
}
