package org.jresearch.kafka.aitune.producer.service;

public class ProducerException extends RuntimeException{

	private static final long serialVersionUID = -3734062628487215055L;

	public ProducerException(String message, Throwable cause) {
		super(message, cause);
	}	
}
