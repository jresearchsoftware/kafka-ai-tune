package org.jresearch.kafka.aitune.producer.content;

public class NullContentProvider implements ContentProvider<byte[]>{

	@Override
	public byte[] getContent() {
		return null;
	}

}
