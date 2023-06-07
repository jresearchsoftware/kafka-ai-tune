package org.jresearch.kafka.aitune.producer.content;

import org.apache.commons.lang3.RandomStringUtils;

public class StringContentProvider implements ContentProvider<String>{

	private final int size;
	
	
	public StringContentProvider(int size) {
		super();
		this.size = size;
	}


	@Override
	public String getContent() {
		return RandomStringUtils.randomAlphanumeric(size);
	}

}
