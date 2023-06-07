package org.jresearch.kafka.aitune.client.serde;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.jresearch.kafka.aitune.client.model.RunnerConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RunnerConfigDeserializer implements Deserializer<RunnerConfig> {

	private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public RunnerConfig deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            return objectMapper.readValue(new String(data, "UTF-8"), RunnerConfig.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to RunnerConfig");
        }
    }

    @Override
    public void close() {
    }
}
