package org.jresearch.kafka.aitune.app.conf;

import java.util.List;

import javax.annotation.PostConstruct;

import org.jresearch.kafka.aitune.runner.model.RunnerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource(value = "file:${WORKLOAD_DIR}/runners.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "configuration")
@Data
public class RunnerConfigurations {
	
	private List<RunnerConfig> runners;
	
	@Autowired
	private WorkloadConfigurations workloads;
	
	@Autowired
	private ProducerConfigurations producers;
	
	@PostConstruct
	protected void init() {
		runners.stream().forEach(r->{
			r.setProducerConfig(producers.get(r.getProducerName()).orElseThrow(()->new ConfigNotFoundException(ConfigEntity.producer, r.getProducerName())));
			r.setWorkloadConfig(workloads.get(r.getWorkloadName()).orElseThrow(()->new ConfigNotFoundException(ConfigEntity.workload, r.getWorkloadName())));
		});
	}
	
}
