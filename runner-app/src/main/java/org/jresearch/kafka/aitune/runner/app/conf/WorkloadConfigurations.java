package org.jresearch.kafka.aitune.runner.app.conf;

import java.util.HashMap;
import java.util.Optional;

import org.jresearch.kafka.aitune.client.conf.YamlPropertySourceFactory;
import org.jresearch.kafka.aitune.client.model.WorkloadConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource(value = "file:${WORKLOADS}", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "configuration")
@Data
public class WorkloadConfigurations {

    private HashMap<String, WorkloadConfig> workloads;
    
    public Optional<WorkloadConfig> get(String workloadName) {
    	return Optional.ofNullable(workloads.get(workloadName));
    }
    
}