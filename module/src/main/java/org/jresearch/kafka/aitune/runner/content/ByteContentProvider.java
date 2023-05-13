package org.jresearch.kafka.aitune.runner.content;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteContentProvider implements ContentProvider<byte[]> {

	private final int messageSize;

	public ByteContentProvider(int messageSize) {
		super();
		this.messageSize = messageSize;
	}

	@Override
	public byte[] getContent() {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Random rd = new Random();
			byte[] randomMsg = new byte[messageSize];
			rd.nextBytes(randomMsg);
			out.write(randomMsg);
			return out.toByteArray();
		} catch (IOException e) {
			log.error("Exception happend during payload generation", e);
		}
		return new byte[0];
	}

}
