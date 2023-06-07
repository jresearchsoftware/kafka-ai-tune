package org.jresearch.kafka.aitune.producer.service;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.ConsumerGroupState;
import org.springframework.beans.factory.annotation.Value;
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