package org.jresearch.kafka.aitune.runner.app;

import org.jresearch.kafka.aitune.runner.service.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
@Import(AppConfig.class)
@EnableScheduling
public class Application {

	@SuppressWarnings("resource")
	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
