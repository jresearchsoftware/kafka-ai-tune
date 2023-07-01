package org.jresearch.kafka.aitune.runner.service;

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
@Table(name = "experiment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Experiment {

	@Id
	@Column(name = "experiment_id")
	private String experimentId;

	@Column(name = "start_time")
	private Instant startTime;

}
