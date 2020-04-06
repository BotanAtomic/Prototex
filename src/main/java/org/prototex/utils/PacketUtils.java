package org.prototex.utils;

import org.prototex.packet.PacketMapper;

/**
 * Created by Botan on 4/6/2020. 10:53 PM
 **/
public class PacketUtils {

    public static int extractId(Class<?> clazz) {
        if (clazz.isAnnotationPresent(PacketMapper.class))
            return clazz.getAnnotation(PacketMapper.class).id();
        return -1;
    }

}
