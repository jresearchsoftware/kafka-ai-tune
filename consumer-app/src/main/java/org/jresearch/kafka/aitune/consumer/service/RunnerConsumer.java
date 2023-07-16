package org.jresearch.kafka.aitune.consumer.service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.jresearch.kafka.aitune.client.conf.NameUtil;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.client.service.MetricService;
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

	private final KafkaTemplate<String, String> kafkaTemplate;
	
	private final ConsumerAppConfig config;
	
	@KafkaListener(topics = "#{config.adminReqTopic}", groupId = "#{config.consumerConsumerGroup}")
	public void consume(@Payload RunnerConfig r, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String experimentId,@Header(KafkaHeaders.CORRELATION_ID) String correlationId) {
		if (r.getConsumerName() != null) {
			log.info("Starting consumer service");
			metricService.startExperiment(experimentId, NameUtil.getConsumerClientId(experimentId, r));
			ConcurrentMessageListenerContainer<?,?>container = kafkaListenerService.getListener(experimentId, r);
			container.start();
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			Runnable cancelTask = () -> {
				container.stop();
				log.info("Finishing loading for topic {}", r.getTopic());
				ProducerRecord<String, String> producerRecord = new ProducerRecord<>(config.getAdminResTopic(),experimentId, experimentId); 
				producerRecord.headers().add(KafkaHeaders.CORRELATION_ID, correlationId.getBytes(StandardCharsets.UTF_8)); 
				kafkaTemplate.send(producerRecord);
			};
			executor.schedule(cancelTask, r.getWorkloadConfig().getTimeInSec(), TimeUnit.SECONDS);
			executor.shutdown();
		}
	}
}