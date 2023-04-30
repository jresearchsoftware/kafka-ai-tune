package org.jresearch.kafka.aitune.runner.service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.jresearch.kafka.aitune.runner.model.RunnerConfig;
import org.jresearch.kafka.aitune.runner.model.WorkloadConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

	@Value("${bootstrap.servers}")
	private String bootstrapServers;

	private AdminClient kafkaAdminClient;

	protected void createTopic(RunnerConfig runnerConfig) {
		WorkloadConfig config = runnerConfig.getWorkloadConfig();
		try {
			if (kafkaAdminClient.listTopics().names().get(20, TimeUnit.SECONDS).contains(runnerConfig.getTopic())) {
				removeTopic(runnerConfig);
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new WorkloadException("Unable to create kafka admin", e);
		}
		NewTopic topic = TopicBuilder.name(runnerConfig.getTopic()).partitions(config.getPartitions())
				.replicas(config.getReplicationFactor()).build();
		kafkaAdminClient.createTopics(Collections.singleton(topic)).all();
	}

	protected void removeTopic(RunnerConfig runnerConfig) {
		kafkaAdminClient.deleteTopics(Collections.singleton(runnerConfig.getTopic())).all();
	}
}
