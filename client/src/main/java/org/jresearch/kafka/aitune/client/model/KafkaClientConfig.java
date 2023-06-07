package org.jresearch.kafka.aitune.client.model;

import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class KafkaClientConfig {
	private String name;
	private Properties props;
}
