package org.prototex.serialization.fields;

import org.prototex.configuration.PrototexConfiguration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ObjectMapperRegistry extends ConcurrentHashMap<Class<?>, Function<byte[], Object>> {

    public ObjectMapperRegistry(PrototexConfiguration configuration) {
        super.put(String.class, bytes -> new String(bytes, configuration.getCharset()));
    }

}
