package org.jresearch.kafka.aitune.client.service;

import org.jresearch.kafka.aitune.client.model.ClientExperiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository  extends JpaRepository<ClientExperiment, String> {

}
