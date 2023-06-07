package org.jresearch.kafka.aitune.client.serde;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RunnerConfigSerializer implements Serializer<RunnerConfig>{

	 private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {		
	}

	@Override
	public byte[] serialize(String topic, RunnerConfig data) {
        try {
            if (data == null){
                System.out.println("Null received at serializing");
                return null;
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing MessageDto to byte[]");
        }
	}

	@Override
	public void close() {}

}
