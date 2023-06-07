package org.jresearch.kafka.aitune.runner.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.ConsumerGroupState;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;
import org.jresearch.kafka.aitune.client.model.WorkloadConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {

	@Value("${bootstrap.servers}")
	private String bootstrapServers;

	@Value("${admin.timeout.ms:10000}")
	private long adminTimeout;

	private Admin kafkaAdminClient;

	@PostConstruct
	public void init() {
		Properties properties = new Properties();
		properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		kafkaAdminClient = Admin.create(properties);
	}

	public void createTopic(RunnerConfig runnerConfig) {
		WorkloadConfig config = runnerConfig.getWorkloadConfig();
		createTopic(runnerConfig.getTopic(),config.getPartitions(), config.getReplicationFactor());
	}
	
	public void createTopic(String name, int partitions, int replicationFactor) {
		NewTopic topic = TopicBuilder.name(name).partitions(partitions)
				.replicas(replicationFactor).build();
		try {
			kafkaAdminClient.createTopics(Collections.singleton(topic)).all().get(adminTimeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.error("Error while creating topic {} ", topic, e);
			throw new KafkaException("Error while creating topic", e);
		}
	}

	public void removeTopic(RunnerConfig runnerConfig) {
		try {
			kafkaAdminClient.deleteTopics(Collections.singleton(runnerConfig.getTopic())).all().get(adminTimeout,
					TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.error("Error while removing topic {} ", runnerConfig.getTopic(), e);
			throw new KafkaException("Error while removing topic", e);
		}
		;
	}
	
	public boolean topicExists(String name) {
		try {
			return kafkaAdminClient.listTopics().names().get(adminTimeout,
					TimeUnit.MILLISECONDS).stream().anyMatch(topicName -> topicName.equals(name));
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.error("Error while checking existence of topic {} ", name, e);
			throw new KafkaException("Error while removing topic", e);
		}
	}

	public boolean consumersReady(Collection<String> consumerGroups) {
		return kafkaAdminClient.describeConsumerGroups(consumerGroups).describedGroups().values().stream().filter(g -> {
			try {
				return g.get(adminTimeout, TimeUnit.MILLISECONDS).state() != ConsumerGroupState.STABLE;
			} catch (Exception e) {
				return false;
			}
		}).findAny().isEmpty();
	}
}
