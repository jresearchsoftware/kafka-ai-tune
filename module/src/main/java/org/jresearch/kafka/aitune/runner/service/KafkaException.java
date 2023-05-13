package org.jresearch.kafka.aitune.runner.service;

public class KafkaException extends RuntimeException{

	private static final long serialVersionUID = -1001613979881709083L;

	public KafkaException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
