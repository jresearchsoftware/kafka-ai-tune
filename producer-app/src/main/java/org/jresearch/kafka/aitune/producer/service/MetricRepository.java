package org.jresearch.kafka.aitune.producer.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository  extends JpaRepository<ClientExperiment, String> {

}
