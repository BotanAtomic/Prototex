package org.prototex.serialization.binary;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Data
public class BinaryField {

    private final Field field;
    private final Annotation annotation;
    private final int index;

}
