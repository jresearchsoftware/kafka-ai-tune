package org.jresearch.kafka.aitune.producer.service;

import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.client.model.WorkloadConfig;
import org.jresearch.kafka.aitune.producer.content.ContentProvider;
import org.springframework.kafka.core.KafkaTemplate;

import com.google.common.util.concurrent.RateLimiter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProducerService<K, V> implements Runnable {

	protected final RunnerConfig runnerConfig;

	protected final KafkaTemplate<K, V> kafkaTemplate;

	protected final ContentProvider<K> keyProvider;

	protected final ContentProvider<V> valueProvider;

	public void run() {
		log.info("Starting to produce messages...");
		WorkloadConfig wlConfig = runnerConfig.getWorkloadConfig();
		RateLimiter limiter = RateLimiter.create(wlConfig.getMessageRate());
		while (true) {
			if (Thread.interrupted()) {
				return;
			}
			limiter.acquire();
			kafkaTemplate.send(runnerConfig.getTopic(), keyProvider.getContent(), valueProvider.getContent());
		}
	}

}