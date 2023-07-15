package org.jresearch.kafka.aitune.producer.service;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jresearch.kafka.aitune.client.conf.NameUtil;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.client.service.MetricService;
import org.jresearch.kafka.aitune.producer.content.ContentProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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

	private final KafkaTemplateService kafkaTemplateService;
	
	private final ContentProviderService contentProviderService;
	
	private final AdminService adminService;
		
	private final MetricService metricService;
	
	private final ProducerAppConfig config;
	
	@KafkaListener(topics = "#{config.adminReqTopic}", groupId = "#{config.producerConsumerGroup}")
	public void consume(@Payload RunnerConfig r,  @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String experimentId) {
		while (!adminService.consumersReady(r.getWaitForConsumerGroups())) {
			try {
				Thread.sleep(config.getWaitForConsumerDelay());
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
			metricService.startExperiment(experimentId,  NameUtil.getProducerClientId(experimentId, r));
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
			Future future = executor.submit(producerService);
			Runnable cancelTask = () -> future.cancel(true);

			executor.schedule(cancelTask, r.getWorkloadConfig().getTimeInSec(), TimeUnit.SECONDS);
			executor.shutdown();
		}

	}
}