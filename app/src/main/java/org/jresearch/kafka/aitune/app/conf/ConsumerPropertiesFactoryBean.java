package org.jresearch.kafka.aitune.app.conf;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jresearch.kafka.aitune.runner.model.KafkaClientConfig;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.CollectionFactory;


public class ConsumerPropertiesFactoryBean extends YamlPropertiesFactoryBean{

	@Override
	protected Properties createProperties() {
		Properties result = CollectionFactory.createStringAdaptingProperties();
		HashMap<String, KafkaClientConfig> configMap = new HashMap<>();
		process((properties, map) -> {
			Object clientMap = map.get(ConfigAttributes.consumers.name());
			if(clientMap == null || (!(clientMap instanceof Map))) {
				throw new ConfigurationException("Cannot parse client configuration, check the format");
			}
			Object clientConfigMap = ((Map)clientMap).get(ConfigAttributes.configs.name());
			if(configMap == null || (!(configMap instanceof Map))) {
				throw new ConfigurationException("Cannot parse client configuration, check the format");
			}
			List<Map<String,String>> clientConfigs = (List)clientConfigMap;
			clientConfigs.stream().forEach(e->{
				String configname = e.keySet().iterator().next();
				final Properties p = new Properties();
				try {
					p.load(new StringReader(e.values().iterator().next()));
				} catch (IOException ex) {
					throw new ConfigurationException("Unable to parse client configuration", ex);
				}
				
				configMap.put(configname, new KafkaClientConfig(configname, p));
			});
		});
		result.put(ConfigAttributes.consumerConfigs.name(), configMap);
		return result;
	}

	
}
