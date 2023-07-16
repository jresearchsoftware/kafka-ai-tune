package org.jresearch.kafka.aitune.runner.service;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RunnerService {

	@Autowired
	@NonNull
	protected final KafkaTemplate<String, RunnerConfig> kafkaTemplate;
	
	public void send(String adminTopic, String experimentId, RunnerConfig conf,String correlationId) {
		log.trace("Sending record: experimentId - {}, correlationId- {}, runner - {}", experimentId,correlationId,conf);
		ProducerRecord<String, RunnerConfig> producerRecord = new ProducerRecord<>(adminTopic,experimentId, conf); 
		producerRecord.headers().add(KafkaHeaders.CORRELATION_ID, correlationId.getBytes(StandardCharsets.UTF_8)); 
		kafkaTemplate.send(producerRecord);
	}
}
