package org.jresearch.kafka.aitune.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jresearch.kafka.aitune.module.service.ModuleService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AppService {

	private ModuleService moduleService;

	public void method() {
		log.warn("Call method from the application service");
		moduleService.method();
	}

}
