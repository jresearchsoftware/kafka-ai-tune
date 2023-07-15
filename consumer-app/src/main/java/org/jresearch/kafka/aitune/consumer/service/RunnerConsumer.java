package org.jresearch.kafka.aitune.consumer.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jresearch.kafka.aitune.client.conf.NameUtil;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.client.service.MetricService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RunnerConsumer {

	private final KafkaListenerService kafkaListenerService;

	private final MetricService metricService;

	@Value("${wait.consumers.delay.ms:2000}")

	private long waitForConsumerDelay;

	private final KafkaTemplate<String, String> kafkaTemplate;

	@KafkaListener(topics = "_benchmark_req", groupId = "group_id1")
	public void consume(@Payload RunnerConfig r, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String experimentId) {
		if (r.getConsumerName() != null) {
			log.info("Starting consumer service");
			metricService.startExperiment(experimentId, NameUtil.getConsumerClientId(experimentId, r));
			ConcurrentMessageListenerContainer container = kafkaListenerService.getListener(experimentId, r);
			container.start();
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			Runnable cancelTask = () -> {
				container.stop();
				log.info("Finishing loading for topic {}", r.getTopic());
				kafkaTemplate.send("_benchmark_res", experimentId, r.getTopic());
				executor.shutdown();

			};
			executor.schedule(cancelTask, r.getWorkloadConfig().getTimeInSec(), TimeUnit.SECONDS);
		}
	}
}