package org.prototex.utils;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionUtils {

    public static List<Class<?>> getAnnotatedClass(String packageName, Class<? extends Annotation> annotationClass) throws IOException {
        ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        return classPath.getTopLevelClassesRecursive(packageName)
                .stream()
                .map(ClassPath.ClassInfo::load)
                .filter(clazz -> clazz.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked casts")
    public static <T> T newInstance(Class<? extends T> clazz, Object... availableArguments) throws Exception {
        Map<Class<?>, Object> args = Stream.of(availableArguments).collect(Collectors.toMap(
                Object::getClass,
                b -> b
        ));
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Object[] arguments = new Object[constructor.getParameterTypes().length];
            int i = 0;
            for (Class<?> parameterType : constructor.getParameterTypes()) {
                arguments[i++] = args.get(parameterType);
            }
            return (T) constructor.newInstance(arguments);
        }
        return null;
    }

}
