package org.jresearch.kafka.aitune.client.conf;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BaseAppConfig {

	@Value("${admin.reqTopic:_benchmark_req}")
	private String adminReqTopic;
	
	@Value("${admin.resTopic:_benchmark_res}")
	private String adminResTopic;
	
	@Value("${runner.consumerGroup:_runner}")
	private String runnerConsumerGroup;
	
	@Value("${producer.consumerGroup:_consumer}")
	private String producerConsumerGroup;
	
	@Value("${consumer.consumerGroup:_producer}")
	private String consumerConsumerGroup;
	
}
