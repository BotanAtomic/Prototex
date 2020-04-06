package org.prototex.packet;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.exception.InvalidPacketException;
import org.prototex.exception.PacketException;
import org.prototex.packet.api.PacketInterface;
import org.prototex.serialization.GenericPacketSerialization;
import org.prototex.serialization.json.JsonSerialization;
import org.prototex.session.PrototexSession;
import org.prototex.utils.PacketUtils;
import org.prototex.utils.ReflectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;

@Slf4j
@Data
public class PacketRegistry {

    private final Map<Integer, GenericPacketSerialization> packets = Maps.newConcurrentMap();

    private final Map<Class<? extends Annotation>, Class<? extends GenericPacketSerialization>> serializers = Maps.newConcurrentMap();

    private final PrototexConfiguration configuration;

    private Class<? extends GenericPacketSerialization> defaultSerializer = JsonSerialization.class;

    public int size() {
        return packets.size();
    }

    public PacketRegistry register(Class<?> clazz) throws PacketException {
        int id = PacketUtils.extractId(clazz);
        if (id != -1) {
            return register(id, clazz);
        } else {
            throw new PacketException("unable to determine id, class not interfaced by @PacketMapper");
        }
    }

    public GenericPacketSerialization getSerializer(Class<?> clazz, Class<? extends GenericPacketSerialization> serializerClass) throws PacketException {
        if (serializerClass == null)
            serializerClass = defaultSerializer;

        try {
            return ReflectionUtils.newInstance(serializerClass, clazz, configuration);
        } catch (Exception e) {
            throw new PacketException(String.format("Can not create new instance of %s", serializerClass.getName()));
        }
    }

    public PacketRegistry register(int id, Class<?> clazz) throws PacketException {
        Class<? extends GenericPacketSerialization> serializerClass = null;

        for (Annotation annotation : clazz.getAnnotations()) {
            if (serializers.containsKey(annotation.annotationType())) {
                serializerClass = serializers.get(annotation.getClass());
                break;
            }
        }

        packets.put(id, getSerializer(clazz, serializerClass));
        return this;
    }

    public PacketRegistry registerPackage(String packageName) throws PacketException, IOException {
        for (Class<?> aClass : ReflectionUtils.getAnnotatedClass(packageName, PacketMapper.class))
            register(aClass);
        return this;
    }

    public PacketRegistry registerSerializationAdapter(Class<? extends Annotation> annotation, Class<? extends GenericPacketSerialization> serializerAdapter) {
        serializers.put(annotation, serializerAdapter);
        return this;
    }

    public Object handle(PrototexSession session, Packet packet) throws Exception {
        GenericPacketSerialization serialization = packets.get(packet.getId());

        if (serialization != null) {
            Object buildClass = serialization.fromPacket(packet);

            if (buildClass == null)
                throw new InvalidPacketException(packet);

            if (buildClass instanceof PacketInterface) {
                ((PacketInterface) buildClass).handle(session, packet);
            }

            return buildClass;
        }

        return null;
    }

}
