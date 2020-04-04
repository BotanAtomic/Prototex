package org.prototex.serialization;

import lombok.Data;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.serialization.fields.BinaryFieldBuilderRegistry;
import org.prototex.serialization.fields.ObjectMapperRegistry;

@Data
public class SerializationConfiguration {

    private final PrototexConfiguration configuration;

    private final BinaryFieldBuilderRegistry fieldBuilder = new BinaryFieldBuilderRegistry();

    private final ObjectMapperRegistry objectMapper;

    public SerializationConfiguration(PrototexConfiguration configuration) {
        this.configuration = configuration;
        this.objectMapper = new ObjectMapperRegistry(configuration);
    }

}
