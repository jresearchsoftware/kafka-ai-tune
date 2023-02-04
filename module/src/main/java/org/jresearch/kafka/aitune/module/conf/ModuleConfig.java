package org.jresearch.kafka.aitune.module.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.jresearch.kafka.aitune.module" })
public class ModuleConfig {
	// Module configuration
	// bootstrap by Spring Boot from
	// org.jresearch.kafka.ai-tune.module/src/main/resources/META-INF/spring.factories

}
