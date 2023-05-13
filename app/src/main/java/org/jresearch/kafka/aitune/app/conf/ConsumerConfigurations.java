package org.jresearch.kafka.aitune.app.conf;

import java.util.Map;
import java.util.Optional;

import org.jresearch.kafka.aitune.runner.model.ConsumerClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource(value = "file:${CONSUMER_CONFIG}", factory = ConsumerPropertySourceFactory.class)
@ConfigurationProperties
@Data
public class ConsumerConfigurations {
	protected Map<String, ConsumerClientConfig> consumerConfigs;

	public Optional<ConsumerClientConfig> get(String name) {
		return Optional.ofNullable(consumerConfigs.get(name));
	}

}
