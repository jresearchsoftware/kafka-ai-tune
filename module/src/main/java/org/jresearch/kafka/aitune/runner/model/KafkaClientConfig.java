package org.jresearch.kafka.aitune.runner.model;

import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class KafkaClientConfig {
	private String name;
	private Properties props;
}
