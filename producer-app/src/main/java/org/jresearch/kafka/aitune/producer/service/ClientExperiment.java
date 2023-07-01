package org.jresearch.kafka.aitune.producer.service;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client_experiment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientExperiment {

	@Id
	@Column(name = "client_id")
	private String clientId;

	@Column(name = "client_start_time")
	private Instant clientStartTime;

	@Column(name = "experiment_id")
	private String experimentId;
}
