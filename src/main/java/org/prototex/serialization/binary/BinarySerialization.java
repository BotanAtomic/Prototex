package org.prototex.serialization.binary;

import org.prototex.packet.Packet;
import org.prototex.serialization.GenericPacketSerialization;
import org.prototex.serialization.SerializationConfiguration;
import org.prototex.serialization.fields.BinaryFieldBuilder;
import org.prototex.stream.BinaryInputStream;
import org.prototex.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class BinarySerialization extends GenericPacketSerialization {

    private final SerializationConfiguration serializationConfiguration;

    public BinarySerialization(Class<?> clazz, SerializationConfiguration serializationConfiguration) {
        super(clazz);
        this.serializationConfiguration = serializationConfiguration;
    }

    @Override
    public Packet toPacket(int id, Object object) throws Exception {
        return null;
    }

    private List<MetaBinaryField> orderedBinaryFields(Object instance) throws Exception {
        List<MetaBinaryField> fields = new ArrayList<>();

        for (Field field : instance.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Annotation annotation = Arrays.stream(field.getAnnotations())
                    .filter(a -> serializationConfiguration.getFieldBuilder().containsKey(a.annotationType()))
                    .findAny().orElse(null);

            if (annotation != null)
                fields.add(new MetaBinaryField(field, annotation, (Integer) ReflectionUtils.getAnnotationIndex(annotation)));
        }

        fields.sort(Comparator.comparingInt(MetaBinaryField::getIndex));
        return fields;
    }

    @Override
    public Object fromPacket(Packet packet) throws Exception {
        Object instance = clazz.newInstance();

        BinaryInputStream stream = new BinaryInputStream(packet.getData());

        for (MetaBinaryField field : orderedBinaryFields(instance)) {
            BinaryFieldBuilder builder = serializationConfiguration.getFieldBuilder().get(field.getAnnotation().annotationType());
            AtomicReference<Object> value = new AtomicReference<>(builder.build(instance, stream, field.getAnnotation()));

            if (value.get() == null)
                continue;

            if (value.get() instanceof byte[]) {
                Optional.ofNullable(serializationConfiguration.getObjectMapper().get(field.getField().getType()))
                        .ifPresent(mapper -> value.set(mapper.apply((byte[]) value.get())));
            }

            field.getField().set(instance, value.get());
        }

        return instance;
    }

}
