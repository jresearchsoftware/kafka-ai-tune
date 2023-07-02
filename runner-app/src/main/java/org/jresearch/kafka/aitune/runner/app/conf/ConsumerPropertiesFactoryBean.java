package org.jresearch.kafka.aitune.runner.app.conf;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jresearch.kafka.aitune.client.conf.ConfigAttributes;
import org.jresearch.kafka.aitune.client.conf.ConfigurationException;
import org.jresearch.kafka.aitune.client.model.ConsumerClientConfig;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.CollectionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerPropertiesFactoryBean extends YamlPropertiesFactoryBean{

	@Override
	protected Properties createProperties() {
		log.info("Creating consumer properties ...");
		Properties result = CollectionFactory.createStringAdaptingProperties();
		HashMap<String, ConsumerClientConfig> configMap = new HashMap<>();
		process((properties, map) -> {
			Object clientMap = map.get(ConfigAttributes.consumers.name());
			if(clientMap == null || (!(clientMap instanceof Map))) {
				throw new ConfigurationException("Cannot parse client configuration, check the format");
			}
			Object clientConfigMap = ((Map)clientMap).get(ConfigAttributes.configs.name());
			if(configMap == null || (!(configMap instanceof Map))) {
				throw new ConfigurationException("Cannot parse client configuration, check the format");
			}
			List<Map<String,Object>> clientConfigs = (List)clientConfigMap;
			clientConfigs.stream().forEach(e->{
				String configname = e.keySet().iterator().next();
				Map<String,Object> consumerConfig = (Map<String, Object>) e.values().iterator().next();
				int concurrency = 1;
				if(consumerConfig.containsKey(ConfigAttributes.concurrency.name())) {
					concurrency = (Integer)consumerConfig.get(ConfigAttributes.concurrency.name());
				}
				
				String consumerValues = (String)consumerConfig.get(ConfigAttributes.consumer.name());
				final Properties p = new Properties();
				try {
					p.load(new StringReader(consumerValues));
				} catch (IOException ex) {
					throw new ConfigurationException("Unable to parse client configuration", ex);
				}
				configMap.put(configname, new ConsumerClientConfig(configname, p, concurrency));
			});
		});
		log.debug("Parsed config map {}", configMap);
		result.put(ConfigAttributes.consumerConfigs.name(), configMap);
		return result;
	}

	
}
