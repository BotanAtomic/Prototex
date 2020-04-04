package org.prototex.utils;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static List<Class<?>> getAnnotatedClass(String packageName, Class<? extends Annotation> annotationClass) throws IOException {
        ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        return classPath.getTopLevelClassesRecursive(packageName)
                .stream()
                .map(ClassPath.ClassInfo::load)
                .filter(clazz -> clazz.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }

    public static Object extractField(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        if (!field.isAccessible())
            field.setAccessible(true);
        return field.get(instance);
    }

    public static Object getAnnotationValue(Annotation annotation) throws Exception {
        Class<?> annotationClass = annotation.getClass();
        return annotationClass.getDeclaredMethod("value").invoke(annotation);
    }

    public static boolean hasMethod(String methodName, Class<?> clazz, Class<?>... parameterTypes) {
        try {
            clazz.getDeclaredMethod(methodName, parameterTypes);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }


}
