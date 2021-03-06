package org.prototex.serialization;

import lombok.RequiredArgsConstructor;
import org.prototex.packet.Packet;

@RequiredArgsConstructor
public abstract class GenericPacketSerialization {

    protected final Class<?> clazz;

    public abstract Packet toPacket(int id, Object object) throws Exception;

    public abstract Object fromPacket(Packet packet) throws Exception;
}
