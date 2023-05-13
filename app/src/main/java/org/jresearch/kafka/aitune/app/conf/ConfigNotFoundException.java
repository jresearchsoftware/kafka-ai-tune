package org.jresearch.kafka.aitune.app.conf;

import lombok.Getter;

@Getter
public class ConfigNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 5241988738560557117L;

	private final ConfigEntity entity;
	
	private final String name;

	public ConfigNotFoundException(ConfigEntity entity, String name) {
		super(entity.name() + ": " + name + " is not found");
		this.entity = entity;
		this.name = name;
	}
	
	
}
