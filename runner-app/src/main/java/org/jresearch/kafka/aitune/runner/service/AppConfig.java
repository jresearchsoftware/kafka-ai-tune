package org.jresearch.kafka.aitune.runner.service;

import org.jresearch.kafka.aitune.client.conf.BaseAppConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

import lombok.Data;

@Configuration(value = "config")
@Data
@EnableKafka
@ComponentScan(basePackages = { "org.jresearch.kafka.aitune.runner.service" })
@EnableJpaRepositories(basePackages = { "org.jresearch.kafka.aitune.runner.service" })
@EntityScan(basePackages = { "org.jresearch.kafka.aitune.runner.service" })
public class AppConfig extends BaseAppConfig{
}
