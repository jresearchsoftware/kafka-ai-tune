package org.jresearch.kafka.aitune.app.conf;

import java.util.Map;
import java.util.Optional;

import org.jresearch.kafka.aitune.runner.model.ProducerClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource(value = "file:${PRODUCER_CONFIG_DIR}/producers.yml", factory = ProducerPropertySourceFactory.class)
@ConfigurationProperties
@Data
public class ProducerConfigurations {

	private Map<String, ProducerClientConfig> clientConfigs;
	
	public Optional<ProducerClientConfig> get(String name) {
		return Optional.ofNullable(clientConfigs.get(name));
	}

}
