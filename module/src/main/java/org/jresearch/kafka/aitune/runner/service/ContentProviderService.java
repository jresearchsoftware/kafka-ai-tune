package org.jresearch.kafka.aitune.runner.service;

import java.io.File;
import java.io.IOException;

import org.jresearch.kafka.aitune.runner.content.AvroContentProvider;
import org.jresearch.kafka.aitune.runner.content.ByteContentProvider;
import org.jresearch.kafka.aitune.runner.content.ContentProvider;
import org.jresearch.kafka.aitune.runner.model.MessageType;
import org.jresearch.kafka.aitune.runner.model.WorkloadConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContentProviderService {

	@Value("#{environment.SCHEMA_DIR}")
	private File schemaDir;

	public ContentProvider<?> getContentProvider(WorkloadConfig wl, boolean isKey) {
		MessageType type = isKey ? wl.getKeyType() : wl.getValueType();
		switch (type) {
		case BYTE:
			return new ByteContentProvider(wl.getMessageSize());
		case AVRO:
			String schemaFile = isKey ? wl.getKeySchemaFile() : wl.getValueSchemaFile();
			try {
				return new AvroContentProvider(new File(schemaDir, schemaFile));
			} catch (IOException e) {
				throw new IllegalArgumentException("Unable to create avro schema ", e);
			}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}

}
