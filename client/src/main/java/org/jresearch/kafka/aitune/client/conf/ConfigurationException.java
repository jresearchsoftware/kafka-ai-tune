package org.jresearch.kafka.aitune.client.conf;

public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 2845261534223157340L;

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

}
