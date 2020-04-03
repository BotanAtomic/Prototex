package org.prototex.packet.serialization;

import org.prototex.packet.Packet;


public class BinarySerialization extends GenericPacketSerialization {

    public BinarySerialization(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Object build(Packet packet) {
        return null;
    }
}
