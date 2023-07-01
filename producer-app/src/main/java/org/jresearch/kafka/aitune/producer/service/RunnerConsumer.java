package org.jresearch.kafka.aitune.producer.service;

import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.producer.content.ContentProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RunnerConsumer {

	private final KafkaTemplateService kafkaTemplateService;
	
	private final ContentProviderService contentProviderService;
	
	private final AdminService adminService;
		
	private final MetricService metricService;
	
	@Value("${wait.consumers.delay.ms:2000}")
	private long waitForConsumerDelay;
	
	@KafkaListener(topics = "_benchmark", groupId = "group_id")
	public void consume(@Payload RunnerConfig r,  @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String experimentId) {
		while (!adminService.consumersReady(r.getWaitForConsumerGroups())) {
			try {
				Thread.sleep(waitForConsumerDelay);
				log.info("Consumers are not ready ...");
			} catch (InterruptedException e) {
				throw new ProducerException("Error while waiting for consumers", e);
			}
		}
		if (r.getProducerName() != null) {
			log.info("Starting producer service");
			KafkaTemplate template = kafkaTemplateService.getTemplate(experimentId,r);
			ContentProvider keyProvier = contentProviderService.getKeyContentProvider(r.getWorkloadConfig());
			ContentProvider valueProvier = contentProviderService.getValueContentProvider(r.getWorkloadConfig());
			ProducerService producerService = new ProducerService<>(r, template, keyProvier, valueProvier);
			metricService.startExperiment(experimentId, r);
			producerService.run();
		}

	}
}