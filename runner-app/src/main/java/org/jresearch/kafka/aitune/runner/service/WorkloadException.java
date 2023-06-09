package org.jresearch.kafka.aitune.runner.service;

public class WorkloadException extends RuntimeException {

	private static final long serialVersionUID = 4424945641648093651L;

	public WorkloadException(String message, Throwable cause) {
		super(message, cause);
	}

	public WorkloadException(String message) {
		super(message);
	}

	public WorkloadException(Throwable cause) {
		super(cause);
	}

}
