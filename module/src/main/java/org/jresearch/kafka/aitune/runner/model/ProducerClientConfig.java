package org.jresearch.kafka.aitune.runner.model;

import java.util.Properties;

import lombok.Data;

@Data
public class ProducerClientConfig{
	private String name;
	private Properties props;

	public ProducerClientConfig(String name, Properties props) {
		super();
		this.name = name;
		this.props = props;
	}

}
