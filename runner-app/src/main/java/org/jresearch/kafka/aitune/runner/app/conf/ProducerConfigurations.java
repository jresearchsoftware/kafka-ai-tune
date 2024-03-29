package org.jresearch.kafka.aitune.runner.app.conf;

import java.util.Map;
import java.util.Optional;

import org.jresearch.kafka.aitune.client.model.KafkaClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource(value = "file:${PRODUCER_CONFIG}", factory = ProducerPropertySourceFactory.class)
@ConfigurationProperties
@Profile("benchmark")
@Data
public class ProducerConfigurations{
	protected Map<String, KafkaClientConfig> producerConfigs;

	public Optional<KafkaClientConfig> get(String name) {
		return Optional.ofNullable(producerConfigs.get(name));
	}

}
