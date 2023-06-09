package org.jresearch.kafka.aitune.consumer.service;

import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RunnerConsumer {

	private final KafkaListenerService kafkaListenerService;

	private final MeterRegistry registry;

	@Value("${wait.consumers.delay.ms:2000}")
	private long waitForConsumerDelay;

	@KafkaListener(topics = "_benchmark", groupId = "group_id1")
	public void consume(RunnerConfig r) {
		if (r.getConsumerName() != null) {
			log.info("Starting consumer service");
			ConcurrentMessageListenerContainer container = kafkaListenerService.getListener(r);
			container.start();
		}

	}
}