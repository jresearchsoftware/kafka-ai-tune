package org.jresearch.kafka.aitune.runner.app.conf;

import org.jresearch.kafka.aitune.client.conf.YamlPropertySourceFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;

public class ProducerPropertySourceFactory extends YamlPropertySourceFactory {

	@Override
	protected YamlPropertiesFactoryBean getFactoryBean() {
		return new ProducerPropertiesFactoryBean();
	}
}
