package org.jresearch.kafka.aitune.app.conf;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;

public class ProducerPropertySourceFactory extends YamlPropertySourceFactory {

	@Override
	protected YamlPropertiesFactoryBean getFactoryBean() {
		return new ProducerPropertiesFactoryBean();
	}
}
