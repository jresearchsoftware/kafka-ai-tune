package org.jresearch.kafka.aitune.runner.model;

import lombok.Data;

@Data
public class RunnerConfig {

	private String producerName;
	
	private String consumerName;
	
	private String workloadName;
	
	private KafkaClientConfig producerConfig;
	
	private KafkaClientConfig consumerConfig;
	
	private WorkloadConfig workloadConfig;
	
	private String topic;
	
}
