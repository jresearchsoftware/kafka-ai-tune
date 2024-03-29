package org.jresearch.kafka.aitune.producer.content;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class StringSetKeyProvider implements ContentProvider<String>{

	private final int keySetSize;
		
	private final String[] keySet;
	
	public StringSetKeyProvider(int keySetSize,int keyLength) {
		super();
		this.keySetSize = keySetSize;
		this.keySet = new String[keySetSize];
		for (int i = 0; i < keySet.length; i++) {
			keySet[i] = RandomStringUtils.randomAlphanumeric(keyLength); 
		}
	}


	@Override
	public String getContent() {
		return keySet[new Random().nextInt(keySetSize)];
	}

}
