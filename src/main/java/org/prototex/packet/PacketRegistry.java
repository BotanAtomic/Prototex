package org.prototex.packet;

import com.google.common.collect.Maps;
import com.google.common.reflect.Invokable;
import lombok.extern.slf4j.Slf4j;
import org.prototex.event.EventManager;
import org.prototex.exception.PacketRegistryException;
import org.prototex.packet.annotations.BinaryMessage;
import org.prototex.packet.annotations.PacketMapper;
import org.prototex.packet.api.PacketInterface;
import org.prototex.packet.serialization.BinarySerialization;
import org.prototex.packet.serialization.GenericPacketSerialization;
import org.prototex.packet.serialization.JsonSerialization;
import org.prototex.session.PrototexSession;

import java.util.Map;

@Slf4j
public class PacketRegistry {

    private final Map<Integer, GenericPacketSerialization> packets = Maps.newConcurrentMap();
    private EventManager eventManager;

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
            serialization = new BinarySerialization(clazz);
        } else {
            serialization = new JsonSerialization(clazz);
        }

        packets.put(id, serialization);
    }

    public Object handle(PrototexSession session, Packet packet) throws Exception {
        GenericPacketSerialization serialization = packets.get(packet.getId());

        if (serialization != null) {
            Object buildClass = serialization.build(packet);

            if (buildClass != null && buildClass.getClass().getSuperclass().equals(PacketInterface.class)) {
                ((PacketInterface) buildClass).initialize(session, packet);
                ((PacketInterface) buildClass).handle(session, packet);
            }

            return buildClass;
        }

        return null;
    }

}
