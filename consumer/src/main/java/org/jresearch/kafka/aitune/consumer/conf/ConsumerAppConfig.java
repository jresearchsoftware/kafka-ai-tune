package org.jresearch.kafka.aitune.consumer.conf;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.client.serde.RunnerConfigDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
@EnableKafka
@ComponentScan(basePackages = { "org.jresearch.kafka.aitune.consumer.service" })
public class ConsumerAppConfig {

	@Value("${bootstrap.servers}")
	private String bootstrapServers;

	@Bean
	public ConsumerFactory<String, RunnerConfig> consumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RunnerConfigDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, RunnerConfig> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, RunnerConfig> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}
}
