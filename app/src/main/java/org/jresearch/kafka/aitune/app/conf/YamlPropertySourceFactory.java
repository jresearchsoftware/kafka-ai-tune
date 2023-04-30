package org.jresearch.kafka.aitune.app.conf;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
        if(encodedResource == null) {
        	throw new ConfigurationException("Provided resource is null " + encodedResource);
        }
    	YamlPropertiesFactoryBean factory = getFactoryBean();
        factory.setResources(encodedResource.getResource());
        Properties properties = factory.getObject();        
        PropertiesPropertySource pp =  new PropertiesPropertySource(encodedResource.getResource().getFilename(), properties);
        return pp;
    }
    
    protected YamlPropertiesFactoryBean getFactoryBean() {
    	return new YamlPropertiesFactoryBean();
    }
}