package org.jresearch.kafka.aitune.runner.conf;

import org.jresearch.kafka.aitune.client.model.RunnerConfig;

public interface IRunnerConfiguratons {
	
	public RunnerConfig next();
	
	public boolean hasNext();
}
