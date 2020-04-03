package org.prototex.packet.serialization;

import lombok.RequiredArgsConstructor;
import org.prototex.packet.Packet;

@RequiredArgsConstructor
public abstract class GenericPacketSerialization {

    protected final Class<?> clazz;

    public abstract Object build(Packet packet);
}
