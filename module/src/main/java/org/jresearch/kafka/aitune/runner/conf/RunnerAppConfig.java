package org.jresearch.kafka.aitune.runner.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.jresearch.kafka.aitune.runner" })
public class RunnerAppConfig {
	// Module configuration
	// bootstrap by Spring Boot from
	// org.jresearch.kafka.ai-tune.module/src/main/resources/META-INF/spring.factories

//	@Bean
//	public  MeterRegistry getMeterRegistry() {
//		return new SimpleMeterRegistry();
//	}
}
