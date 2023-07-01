package org.jresearch.kafka.aitune.runner.app.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.Data;

@Configuration
@Data
@ComponentScan(basePackages = { "org.jresearch.kafka.aitune.runner.service" })
@EnableJpaRepositories(basePackages = { "org.jresearch.kafka.aitune.runner.service" })
@EntityScan(basePackages = { "org.jresearch.kafka.aitune.runner.service" })
public class AppConfig {

	@Value("${admin.topic:_benchmark}")
	private String adminTopicName;


}
