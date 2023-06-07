package org.jresearch.kafka.aitune.runner.app.conf;

import java.util.List;

import javax.annotation.PostConstruct;

import org.jresearch.kafka.aitune.client.conf.ConfigEntity;
import org.jresearch.kafka.aitune.client.conf.ConfigNotFoundException;
import org.jresearch.kafka.aitune.client.conf.YamlPropertySourceFactory;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource(value = "file:${RUNNER}", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "configuration")
@Data
public class RunnerConfigurations {
	
	private List<RunnerConfig> runners;
	
	@Autowired
	private WorkloadConfigurations workloads;
	
	@Autowired
	private ProducerConfigurations producers;
	
	@Autowired
	private ConsumerConfigurations consumers;

	@PostConstruct
	protected void init() {
		runners.stream().forEach(r->{
			r.setProducerConfig(producers.get(r.getProducerName()).orElse(null));
			r.setWorkloadConfig(workloads.get(r.getWorkloadName()).orElseThrow(()->new ConfigNotFoundException(ConfigEntity.workload, r.getWorkloadName())));
			r.setConsumerConfig(consumers.get(r.getConsumerName()).orElse(null));
		});
	}
	
}
