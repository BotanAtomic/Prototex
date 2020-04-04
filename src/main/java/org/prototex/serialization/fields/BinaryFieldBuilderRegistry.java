package org.prototex.serialization.fields;

import org.prototex.serialization.annotations.value.*;
import org.prototex.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;

import static org.prototex.serialization.fields.BinaryFieldBuilder.getBytesLength;

public class BinaryFieldBuilderRegistry extends ConcurrentHashMap<Class<? extends Annotation>, BinaryFieldBuilder> {

    public BinaryFieldBuilderRegistry() {
        super.put(BooleanValue.class, (instance, stream, annotation) -> stream.readBoolean());
        super.put(ByteValue.class, (instance, stream, annotation) -> stream.readByte());
        super.put(ShortValue.class, (instance, stream, annotation) -> stream.readShort());
        super.put(IntValue.class, (instance, stream, annotation) -> stream.readInt());
        super.put(LongValue.class, (instance, stream, annotation) -> stream.readLong());
        super.put(FloatValue.class, (instance, stream, annotation) -> stream.readFloat());
        super.put(DoubleValue.class, (instance, stream, annotation) -> stream.readDouble());
        super.put(BytesValue.class, (instance, stream, annotation) -> stream.readBytes(getBytesLength(instance, (BytesValue) annotation)));
    }

    public BinaryFieldBuilderRegistry add(Class<? extends Annotation> annotationClass, BinaryFieldBuilder builder) throws RuntimeException {
        if (!ReflectionUtils.hasMethod("value", annotationClass))
            throw new RuntimeException("you must implements value() on your @interface");

        super.put(annotationClass, builder);
        return this;
    }

}
