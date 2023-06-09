package org.jresearch.kafka.aitune.producer.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
@EnableScheduling
public class Application {

	@SuppressWarnings("resource")
	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
