package org.jresearch.kafka.aitune.producer.service;

import org.jresearch.kafka.aitune.client.conf.BaseAppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

import lombok.Data;

@Configuration(value="config")
@Data
@EnableKafka
@EnableJpaRepositories(basePackages = { "org.jresearch.kafka.aitune.producer.service","org.jresearch.kafka.aitune.client.service" })
@EntityScan(basePackages = { "org.jresearch.kafka.aitune.client.model" })
public class ProducerAppConfig extends BaseAppConfig{

	@Value("${wait.consumers.delay.ms:2000}")
	private long waitForConsumerDelay;
}
