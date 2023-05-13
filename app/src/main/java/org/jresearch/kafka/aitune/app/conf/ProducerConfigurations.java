package org.jresearch.kafka.aitune.app.conf;

import java.util.Map;
import java.util.Optional;

import org.jresearch.kafka.aitune.runner.model.KafkaClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource(value = "file:${PRODUCER_CONFIG}", factory = ProducerPropertySourceFactory.class)
@ConfigurationProperties
@Data
public class ProducerConfigurations{
	protected Map<String, KafkaClientConfig> producerConfigs;

	public Optional<KafkaClientConfig> get(String name) {
		return Optional.ofNullable(producerConfigs.get(name));
	}

}
