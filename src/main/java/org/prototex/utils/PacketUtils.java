package org.prototex.utils;

import org.prototex.packet.PacketMapper;

import java.util.Optional;

/**
 * Created by Botan on 4/6/2020. 10:53 PM
 **/
public class PacketUtils {

    public static Optional<Integer> extractId(Class<?> clazz) {
        return Optional.ofNullable(clazz.isAnnotationPresent(PacketMapper.class) ? clazz.getAnnotation(PacketMapper.class).id() : null);
    }

}
