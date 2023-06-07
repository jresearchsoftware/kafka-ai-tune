package org.jresearch.kafka.aitune.client.service;

import org.jresearch.kafka.aitune.client.model.KafkaClientConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import io.micrometer.core.instrument.MeterRegistry;

public class BaseKafkaService implements BeanFactoryAware {

	@Autowired
	protected DefaultListableBeanFactory beanFactory;

	@Value("${bootstrap.servers}")
	protected String bootstrapServers;

	@Autowired
	protected MeterRegistry registry;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}

	protected String getTemplateName(KafkaClientConfig clientConfig) {
		return clientConfig.getName() + "_template";
	}

	protected String getFactoryName(KafkaClientConfig clientConfig) {
		return clientConfig.getName() + "_factory";
	}

	protected void removeTemplate(KafkaClientConfig clientConfig) {
		beanFactory.removeBeanDefinition(getTemplateName(clientConfig));
		beanFactory.removeBeanDefinition(getFactoryName(clientConfig));

	}
}
