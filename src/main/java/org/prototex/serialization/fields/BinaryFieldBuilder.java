package org.prototex.serialization.fields;

import org.prototex.serialization.annotations.value.BytesValue;
import org.prototex.stream.BinaryInputStream;
import org.prototex.utils.ReflectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;

public interface BinaryFieldBuilder {

    Object build(Object instance, BinaryInputStream stream, Annotation annotation) throws Exception;

    static int getBytesLength(Object instance, BytesValue bytesValue) throws Exception {
        if(bytesValue.fieldLength().isEmpty())
            return bytesValue.length();
        return (int) ReflectionUtils.extractField(instance, bytesValue.fieldLength());
    }
}
