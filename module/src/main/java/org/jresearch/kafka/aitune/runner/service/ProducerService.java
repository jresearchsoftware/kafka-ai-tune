package org.jresearch.kafka.aitune.runner.service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.jresearch.kafka.aitune.runner.content.ContentProvider;
import org.jresearch.kafka.aitune.runner.model.RunnerConfig;
import org.jresearch.kafka.aitune.runner.model.WorkloadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import com.google.common.util.concurrent.RateLimiter;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProducerService<K, V> {

	protected final RunnerConfig runnerConfig;

	protected final KafkaTemplate<K, V> kafkaTemplate;

	protected final ContentProvider<K> keyProvider;

	protected final ContentProvider<V> valueProvider;

	private AtomicLong startExpirement = new AtomicLong();

	protected final MeterRegistry registry;

	public ProducerService(RunnerConfig runnerConfig, KafkaTemplate<K, V> kafkaTemplate, ContentProvider<K> keyProvider,
			ContentProvider<V> valueProvider, MeterRegistry registry) {
		super();
		this.runnerConfig = runnerConfig;
		this.kafkaTemplate = kafkaTemplate;
		this.keyProvider = keyProvider;
		this.valueProvider = valueProvider;
		this.registry = registry;
	}

	public void run() {
		registry.more().timeGauge("exp_start", Collections.singleton(Tag.of("client_id", runnerConfig.getProducerName())),
				startExpirement, TimeUnit.SECONDS, AtomicLong::doubleValue);
		startExpirement.set(System.currentTimeMillis() / 1000);
		WorkloadConfig wlConfig = runnerConfig.getWorkloadConfig();
		RateLimiter limiter = RateLimiter.create(wlConfig.getMessageRate());
		for (int i = 0; i < wlConfig.getNumMessages(); i++) {
			limiter.acquire();
			kafkaTemplate.send(runnerConfig.getTopic(), keyProvider.getContent(), valueProvider.getContent());
		}
	}

}