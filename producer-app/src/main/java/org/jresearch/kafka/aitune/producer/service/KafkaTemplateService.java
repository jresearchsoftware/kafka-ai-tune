package org.jresearch.kafka.aitune.producer.service;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jresearch.kafka.aitune.client.model.MessageType;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.client.model.WorkloadConfig;
import org.jresearch.kafka.aitune.client.service.BaseKafkaService;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.MicrometerProducerListener;
import org.springframework.stereotype.Service;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

@Service
public class KafkaTemplateService extends BaseKafkaService {

	public KafkaTemplate<?, ?> getTemplate(String experimentId, RunnerConfig runnerConfig) {

		Properties maps = runnerConfig.getProducerConfig().getProps();
		maps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		
		maps.put(ProducerConfig.CLIENT_ID_CONFIG, NameUtil.getClientId(experimentId, runnerConfig));
		WorkloadConfig wlConfig = runnerConfig.getWorkloadConfig();
		maps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, getSerializerName(wlConfig.getKeyType()));
		maps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, getSerializerName(wlConfig.getValueType()));

		DefaultKafkaProducerFactory<?,?> producerFactory = new DefaultKafkaProducerFactory(maps);
		producerFactory.addListener(new MicrometerProducerListener<>(registry));

		return new KafkaTemplate<>(producerFactory);
	}

	
	protected String getSerializerName(MessageType type) {
		switch (type) {
		case STRING:
			return StringSerializer.class.getName();
		case AVRO:
			return KafkaAvroSerializer.class.getName();
		case BYTE:
		default:
			return ByteArraySerializer.class.getName();
		}
	}

	protected void removeTemplate(RunnerConfig runnerConfig) {
		removeTemplate(runnerConfig.getProducerConfig());
	}

}
