package org.jresearch.kafka.aitune.runner.model;

import lombok.Data;

@Data
public class RunnerConfig {

	private String producerName;
	
	private String workloadName;
	
	private ProducerClientConfig producerConfig;
	
	private WorkloadConfig workloadConfig;
	
	private String topic;
	
}
