package org.jresearch.kafka.aitune.runner.service;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.jresearch.kafka.aitune.runner.model.MessageType;
import org.jresearch.kafka.aitune.runner.model.ProducerClientConfig;
import org.jresearch.kafka.aitune.runner.model.RunnerConfig;
import org.jresearch.kafka.aitune.runner.model.WorkloadConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.MicrometerProducerListener;
import org.springframework.stereotype.Service;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class KafkaTemplateService implements BeanFactoryAware {

	@Autowired
	private DefaultListableBeanFactory beanFactory;

	@Value("${bootstrap.servers}")
	private String bootstrapServers;

	@Autowired
	private MeterRegistry registry;

	public KafkaTemplate<?, ?> getTemplate(RunnerConfig runnerConfig) {

		Properties maps = runnerConfig.getProducerConfig().getProps();
		maps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

		WorkloadConfig wlConfig = runnerConfig.getWorkloadConfig();
		maps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, getSerializerName(wlConfig.getKeyType()));
		maps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, getSerializerName(wlConfig.getValueType()));

		DefaultKafkaProducerFactory<?,?> producerFactory = new DefaultKafkaProducerFactory(maps);
		producerFactory.addListener(new MicrometerProducerListener<>(registry));

		return new KafkaTemplate<>(producerFactory);
	}

	protected String getSerializerName(MessageType type) {
		if (type == MessageType.AVRO) {
			return KafkaAvroSerializer.class.getName();
		} else {
			return ByteArraySerializer.class.getName();
		}
	}

	protected String getTemplateName(ProducerClientConfig producerConfig) {
		return producerConfig.getName() + "_template";
	}

	protected String getFactoryName(ProducerClientConfig producerConfig) {
		return producerConfig.getName() + "_factory";
	}

	protected void removeTemplate(RunnerConfig runnerConfig) {
		ProducerClientConfig producerConfig = runnerConfig.getProducerConfig();
		beanFactory.removeBeanDefinition(getTemplateName(producerConfig));
		beanFactory.removeBeanDefinition(getFactoryName(producerConfig));
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}
}
