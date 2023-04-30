package org.jresearch.kafka.aitune.app.conf;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jresearch.kafka.aitune.runner.model.ProducerClientConfig;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.CollectionFactory;

public class ProducerPropertiesFactoryBean extends YamlPropertiesFactoryBean{

	@Override
	protected Properties createProperties() {
		Properties result = CollectionFactory.createStringAdaptingProperties();
		HashMap<String, ProducerClientConfig> configMap = new HashMap<>();
		process((properties, map) -> {
			Object producerMap = map.get(ConfigAttributes.producers.name());
			if(producerMap == null || (!(producerMap instanceof Map))) {
				throw new ConfigurationException("Cannot parse producer configuration, check the format");
			}
			Object producerClientMap = ((Map)producerMap).get(ConfigAttributes.configs.name());
			if(configMap == null || (!(configMap instanceof Map))) {
				throw new ConfigurationException("Cannot parse producer configuration, check the format");
			}
			List<Map<String,String>> producerConfigs = (List)producerClientMap;
			producerConfigs.stream().forEach(e->{
				String configname = e.keySet().iterator().next();
				final Properties p = new Properties();
				try {
					p.load(new StringReader(e.values().iterator().next()));
				} catch (IOException ex) {
					throw new ConfigurationException("Unable to parse producer configuration", ex);
				}
				
				configMap.put(configname, new ProducerClientConfig(configname, p));
			});
		});
		result.put(ConfigAttributes.clientConfigs.name(), configMap);
		return result;
	}

	
}
