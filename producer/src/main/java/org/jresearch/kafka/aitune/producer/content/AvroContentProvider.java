package org.jresearch.kafka.aitune.producer.content;

import java.io.File;
import java.io.IOException;

import org.apache.avro.generic.GenericRecord;

import io.confluent.avro.random.generator.Generator;

public class AvroContentProvider implements ContentProvider<GenericRecord>{

	private Generator avroGenerator;
	
	
	public AvroContentProvider(final File schemaFile) throws IOException {
		avroGenerator  = new Generator.Builder().schemaFile(schemaFile).build();
	}


	@Override
	public GenericRecord getContent() {
		return (GenericRecord) avroGenerator.generate();
	}

}
