package org.jresearch.kafka.aitune.consumer.service;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jresearch.kafka.aitune.client.model.MessageType;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.client.model.WorkloadConfig;
import org.jresearch.kafka.aitune.client.service.BaseKafkaService;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.MicrometerConsumerListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaListenerService extends BaseKafkaService {

	public ConcurrentMessageListenerContainer<?, ?> getListener(String experimentId, RunnerConfig runnerConfig) {

		Properties maps = runnerConfig.getConsumerConfig().getProps();
		maps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

		String clientId = maps.get(ProducerConfig.CLIENT_ID_CONFIG).toString();
		maps.put(ProducerConfig.CLIENT_ID_CONFIG, String.join("_", experimentId, runnerConfig.getTopic(),clientId));

		WorkloadConfig wlConfig = runnerConfig.getWorkloadConfig();

		Deserializer<?> keyDeserializer = getDeserializer(wlConfig.getKeyType());
		Deserializer<?> valueDeserializer = getDeserializer(wlConfig.getValueType());
		DefaultKafkaConsumerFactory<?, ?> consumerFactory = new DefaultKafkaConsumerFactory(maps, keyDeserializer,
				valueDeserializer);
		consumerFactory.addListener(new MicrometerConsumerListener<>(registry));

		ConcurrentKafkaListenerContainerFactory listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		listenerFactory.setConsumerFactory(consumerFactory);
		
		ContainerProperties containerProps = new ContainerProperties(runnerConfig.getTopic());
		containerProps.setAckMode(AckMode.BATCH);
		containerProps.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Object data) {
				log.trace("Message received: {}", data);
			}

		});

		ConcurrentMessageListenerContainer container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
		container.setConcurrency(runnerConfig.getConsumerConfig().getConcurrency());
		return container;
	}

	protected Deserializer<?> getDeserializer(MessageType type) {
		switch (type) {
		case STRING:
			return new StringDeserializer();
		case AVRO:
			return new KafkaAvroDeserializer();
		case BYTE:
		default:
			return new ByteArrayDeserializer();
		}
	}

	protected void removeTemplate(RunnerConfig runnerConfig) {
		removeTemplate(runnerConfig.getConsumerConfig());
	}
}
