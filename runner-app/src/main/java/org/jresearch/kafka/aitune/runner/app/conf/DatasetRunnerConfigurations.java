package org.jresearch.kafka.aitune.runner.app.conf;

import java.util.Iterator;
import java.util.Properties;

import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.runner.conf.IRunnerConfiguratons;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dataset")
public class DatasetRunnerConfigurations implements IRunnerConfiguratons{

	private Properties workload;
	
	private Properties consumerConf;
	
	private Properties producerConf;
	
	private Iterator<RunnerConfig> iterator;

	
	@Override
	public RunnerConfig next() {
		return iterator.next();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}
	
}
