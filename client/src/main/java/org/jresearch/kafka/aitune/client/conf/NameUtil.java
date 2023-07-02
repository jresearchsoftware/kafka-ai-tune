package org.jresearch.kafka.aitune.client.conf;

import org.jresearch.kafka.aitune.client.model.RunnerConfig;

public class NameUtil {

	public static final String getProducerClientId(String experimentId, RunnerConfig runnerConfig) {
		return String.join("_", experimentId, runnerConfig.getTopic(), runnerConfig.getProducerName());

	}
	
	public static final String getConsumerClientId(String experimentId, RunnerConfig runnerConfig) {
		return String.join("_", experimentId, runnerConfig.getTopic(), runnerConfig.getConsumerName());

	}
}
