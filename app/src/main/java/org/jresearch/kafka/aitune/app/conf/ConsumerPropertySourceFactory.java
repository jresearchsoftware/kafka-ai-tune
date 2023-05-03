package org.jresearch.kafka.aitune.app.conf;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;

public class ConsumerPropertySourceFactory extends YamlPropertySourceFactory {

	@Override
	protected YamlPropertiesFactoryBean getFactoryBean() {
		return new ConsumerPropertiesFactoryBean();
	}
}