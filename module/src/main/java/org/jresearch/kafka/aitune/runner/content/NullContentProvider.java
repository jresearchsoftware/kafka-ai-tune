package org.jresearch.kafka.aitune.runner.content;

public class NullContentProvider implements ContentProvider<byte[]>{

	@Override
	public byte[] getContent() {
		return null;
	}

}
