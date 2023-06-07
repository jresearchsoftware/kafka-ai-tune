package org.jresearch.kafka.aitune.runner.app.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
@ComponentScan(basePackages = { "org.jresearch.kafka.aitune.runner.app" })
public class AppConfig {

	@Value("${admin.topic:_benchmark}")
	private String adminTopicName;


}
