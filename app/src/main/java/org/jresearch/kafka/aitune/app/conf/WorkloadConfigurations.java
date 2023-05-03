package org.jresearch.kafka.aitune.app.conf;

import java.util.HashMap;
import java.util.Optional;

import org.jresearch.kafka.aitune.runner.model.WorkloadConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource(value = "file:${WORKLOAD_DIR}/workloads2.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "configuration")
@Data
public class WorkloadConfigurations {

    private HashMap<String, WorkloadConfig> workloads;
    
    public Optional<WorkloadConfig> get(String workloadName) {
    	return Optional.ofNullable(workloads.get(workloadName));
    }
    
}