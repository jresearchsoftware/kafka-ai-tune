package org.jresearch.kafka.aitune.runner.service;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jresearch.kafka.aitune.runner.content.ByteContentProvider;
import org.jresearch.kafka.aitune.runner.content.StringContentProvider;
import org.jresearch.kafka.aitune.runner.model.MessageType;
import org.jresearch.kafka.aitune.runner.model.RunnerConfig;
import org.jresearch.kafka.aitune.runner.model.WorkloadConfig;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.MicrometerConsumerListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;

@Service
public class KafkaListenerService extends BaseKafkaService {

	public KafkaMessageListenerContainer<?, ?> getListener(RunnerConfig runnerConfig) {

		Properties maps = runnerConfig.getConsumerConfig().getProps();
		maps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

		WorkloadConfig wlConfig = runnerConfig.getWorkloadConfig();

		Deserializer keyDeserializer = getDeserializer(wlConfig.getKeyType());
		Deserializer valueDeserializer = getDeserializer(wlConfig.getValueType());
		DefaultKafkaConsumerFactory<?, ?> consumerFactory = new DefaultKafkaConsumerFactory(maps, keyDeserializer,
				valueDeserializer);
		consumerFactory.addListener(new MicrometerConsumerListener<>(registry));

		ContainerProperties containerProps = new ContainerProperties(runnerConfig.getTopic());
		containerProps.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Object data) {
				System.out.println("invoked");
			}

		});

		return new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
	}

	protected Deserializer getDeserializer(MessageType type) {
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
