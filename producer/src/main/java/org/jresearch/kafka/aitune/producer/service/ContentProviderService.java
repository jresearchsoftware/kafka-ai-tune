package org.jresearch.kafka.aitune.producer.service;

import java.io.File;
import java.io.IOException;

import org.jresearch.kafka.aitune.client.model.KeyDistributionType;
import org.jresearch.kafka.aitune.client.model.MessageType;
import org.jresearch.kafka.aitune.client.model.WorkloadConfig;
import org.jresearch.kafka.aitune.producer.content.AvroContentProvider;
import org.jresearch.kafka.aitune.producer.content.ByteContentProvider;
import org.jresearch.kafka.aitune.producer.content.ContentProvider;
import org.jresearch.kafka.aitune.producer.content.NullContentProvider;
import org.jresearch.kafka.aitune.producer.content.StringContentProvider;
import org.jresearch.kafka.aitune.producer.content.StringSetKeyProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContentProviderService {

	@Value("#{environment.SCHEMA_DIR}")
	private File schemaDir;

	public ContentProvider<?> getValueContentProvider(WorkloadConfig wl) {
		MessageType type = wl.getValueType();
		return getRandomProvider(type, wl.getMessageSize(), wl.getValueSchemaFile());
	}

	protected ContentProvider<?> getRandomProvider(MessageType type, int size, String schemaFile) {
		switch (type) {
		case STRING:
			return new StringContentProvider(size);
		case BYTE:
			return new ByteContentProvider(size);
		case AVRO:
			try {
				return new AvroContentProvider(new File(schemaDir, schemaFile));
			} catch (IOException e) {
				throw new IllegalArgumentException("Unable to create avro schema ", e);
			}

		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}

	public ContentProvider<?> getKeyContentProvider(WorkloadConfig wl) {
		KeyDistributionType distributionType = wl.getKeyDistributionType();
		if (distributionType == null || distributionType == KeyDistributionType.RANDOM) {
			return getRandomProvider(wl.getKeyType(), wl.getKeySize(), wl.getKeySchemaFile());
		}
		if(distributionType == KeyDistributionType.NO_KEY) {
			return new NullContentProvider();
		}
		if(distributionType == KeyDistributionType.FIXED_KEY_SET) {
			return new StringSetKeyProvider(wl.getKeySetSize(), wl.getKeySize());
		}
		throw new IllegalArgumentException("Unexpected value: " + distributionType);
	}

}
