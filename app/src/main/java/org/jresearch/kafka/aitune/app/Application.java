package org.jresearch.kafka.aitune.app;

import org.jresearch.kafka.aitune.app.conf.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AppConfig.class)
public class Application {

	@SuppressWarnings("resource")
	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
