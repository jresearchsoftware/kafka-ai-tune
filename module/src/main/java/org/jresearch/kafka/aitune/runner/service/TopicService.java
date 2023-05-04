package org.jresearch.kafka.aitune.runner.service;

import java.util.Collections;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
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

	private Admin kafkaAdminClient;

	@PostConstruct
	public void init() {
		Properties properties = new Properties();
		properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		kafkaAdminClient = Admin.create(properties);
	}

	public void createTopic(RunnerConfig runnerConfig) {
		WorkloadConfig config = runnerConfig.getWorkloadConfig();
		NewTopic topic = TopicBuilder.name(runnerConfig.getTopic())
				.partitions(config.getPartitions()).replicas(config.getReplicationFactor()).build();
		kafkaAdminClient.createTopics(Collections.singleton(topic)).all();
	}

	public void removeTopic(RunnerConfig runnerConfig) {
		kafkaAdminClient.deleteTopics(Collections.singleton(runnerConfig.getTopic())).all();
	}
}
