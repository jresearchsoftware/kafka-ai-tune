package org.jresearch.kafka.aitune.runner.model;

import lombok.Data;

@Data
public class WorkloadConfig{

	private String name;
	
	private int numMessages;

	private int messageRate;
	
	private int partitions;
	
	private int replicationFactor;
	
	private int messageSize;
	
	private int keySize;
	
	private MessageType keyType;
	
	private MessageType valueType;
		
	private String valueSchemaFile;
	
	private String keySchemaFile;
	
	private KeyDistributionType keyDistributionType;
	
}
