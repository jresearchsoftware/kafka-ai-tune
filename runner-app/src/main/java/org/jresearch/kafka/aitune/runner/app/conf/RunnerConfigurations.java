package org.jresearch.kafka.aitune.runner.app.conf;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jresearch.kafka.aitune.client.conf.ConfigEntity;
import org.jresearch.kafka.aitune.client.conf.ConfigNotFoundException;
import org.jresearch.kafka.aitune.client.conf.YamlPropertySourceFactory;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.runner.conf.IRunnerConfiguratons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource(value = "file:${RUNNER}", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "configuration")
@Data
@Profile("benchmark")
public class RunnerConfigurations implements IRunnerConfiguratons{
	
	private List<RunnerConfig> runners;
	
	@Autowired
	private WorkloadConfigurations workloads;
	
	@Autowired
	private ProducerConfigurations producers;
	
	@Autowired
	private ConsumerConfigurations consumers;
	
	private Iterator<RunnerConfig> iterator;

	@PostConstruct
	protected void init() {
		runners.stream().forEach(r->{
			r.setProducerConfig(producers.get(r.getProducerName()).orElse(null));
			r.setWorkloadConfig(workloads.get(r.getWorkloadName()).orElseThrow(()->new ConfigNotFoundException(ConfigEntity.workload, r.getWorkloadName())));
			r.setConsumerConfig(consumers.get(r.getConsumerName()).orElse(null));
		});
		iterator = runners.iterator();
	}

	@Override
	public RunnerConfig next() {
		return iterator.next();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}
	
}
