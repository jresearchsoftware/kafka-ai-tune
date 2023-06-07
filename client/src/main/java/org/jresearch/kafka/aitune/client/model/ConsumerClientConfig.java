package org.jresearch.kafka.aitune.client.model;

import java.util.Properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ConsumerClientConfig extends KafkaClientConfig{
	
	private int concurrency;

	public ConsumerClientConfig(String name, Properties props, int concurrency) {
		super(name, props);
		this.concurrency = concurrency;
	}

}
