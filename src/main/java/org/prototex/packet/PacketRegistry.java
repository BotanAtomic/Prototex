package org.prototex.packet;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.exception.InvalidPacketException;
import org.prototex.exception.PacketRegistryException;
import org.prototex.packet.api.PacketInterface;
import org.prototex.serialization.GenericPacketSerialization;
import org.prototex.serialization.annotations.PacketMapper;
import org.prototex.serialization.json.JsonSerialization;
import org.prototex.session.PrototexSession;
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

    public PacketRegistry register(Class<?> clazz) throws PacketRegistryException {
        if (clazz.isAnnotationPresent(PacketMapper.class)) {
            PacketMapper mapper = clazz.getAnnotation(PacketMapper.class);
            return register(mapper.id(), clazz);
        } else {
            throw new PacketRegistryException("unable to determine id, class not interfaced by @PacketMapper");
        }
    }

    public PacketRegistry register(int id, Class<?> clazz) throws PacketRegistryException {
        Class<? extends GenericPacketSerialization> serializerClass = defaultSerializer;

        for (Annotation annotation : clazz.getAnnotations()) {
            if (serializers.containsKey(annotation.annotationType())) {
                serializerClass = serializers.get(annotation.getClass());
                break;
            }
        }

        try {
            packets.put(id, ReflectionUtils.newInstance(serializerClass, clazz, configuration));
        } catch (Exception e) {
            throw new PacketRegistryException(String.format("Can not create new instance of %s", serializerClass.getName()));
        }
        return this;
    }

    public PacketRegistry registerPackage(String packageName) throws PacketRegistryException, IOException {
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
