package org.prototex.packet;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.prototex.exception.InvalidPacketException;
import org.prototex.exception.PacketRegistryException;
import org.prototex.packet.api.PacketInterface;
import org.prototex.serialization.GenericPacketSerialization;
import org.prototex.serialization.SerializationConfiguration;
import org.prototex.serialization.annotations.PacketMapper;
import org.prototex.serialization.annotations.message.BinaryMessage;
import org.prototex.serialization.binary.BinarySerialization;
import org.prototex.serialization.json.JsonSerialization;
import org.prototex.session.PrototexSession;
import org.prototex.utils.ReflectionUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Data
public class PacketRegistry {

    private final Map<Integer, GenericPacketSerialization> packets = Maps.newConcurrentMap();

    private final SerializationConfiguration serializationConfiguration;

    public int size() {
        return packets.size();
    }

    public void register(Class<?> clazz) throws PacketRegistryException {
        if (clazz.isAnnotationPresent(PacketMapper.class)) {
            PacketMapper mapper = clazz.getAnnotation(PacketMapper.class);
            register(mapper.id(), clazz);
        } else {
            throw new PacketRegistryException("unable to determine id, did you interface you class by @PacketMapper ?");
        }
    }

    public void register(int id, Class<?> clazz) {
        GenericPacketSerialization serialization;

        if (clazz.isAnnotationPresent(BinaryMessage.class)) {
            serialization = new BinarySerialization(clazz, serializationConfiguration);
        } else {
            serialization = new JsonSerialization(clazz, serializationConfiguration.getConfiguration());
        }

        packets.put(id, serialization);
    }

    public void registerPackage(String packageName) throws PacketRegistryException, IOException {
        for (Class<?> aClass : ReflectionUtils.getAnnotatedClass(packageName, PacketMapper.class))
            register(aClass);
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
