package org.jresearch.kafka.aitune.runner.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RunnerConfig {

	private String producerName;
	
	private String consumerName;
	
	private String workloadName;
	
	private KafkaClientConfig producerConfig;
	
	private ConsumerClientConfig consumerConfig;
	
	private WorkloadConfig workloadConfig;
	
	private String topic;
	
	private boolean shouldCreateTopic;
	
	private boolean shouldRemoveTopic;
	
	private List<String> waitForConsumerGroups = new ArrayList<>();
	
}
