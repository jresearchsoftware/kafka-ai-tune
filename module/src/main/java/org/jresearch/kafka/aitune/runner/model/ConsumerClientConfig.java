package org.jresearch.kafka.aitune.runner.model;

import java.util.Properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsumerClientConfig extends KafkaClientConfig{
	
	private int concurrency;

	public ConsumerClientConfig(String name, Properties props, int concurrency) {
		super(name, props);
		this.concurrency = concurrency;
	}

}
