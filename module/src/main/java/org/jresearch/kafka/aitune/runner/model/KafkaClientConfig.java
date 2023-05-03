package org.jresearch.kafka.aitune.runner.model;

import java.util.Properties;

import lombok.Data;

@Data
public class KafkaClientConfig{
	private String name;
	private Properties props;

	public KafkaClientConfig(String name, Properties props) {
		super();
		this.name = name;
		this.props = props;
	}

}
