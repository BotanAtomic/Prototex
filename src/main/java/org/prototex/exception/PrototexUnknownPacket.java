package org.prototex.exception;

import org.prototex.packet.Packet;

public class PrototexUnknownPacket extends PrototexException {

    public PrototexUnknownPacket(Packet packet) {
        super(String.format("unknown incoming packet %s", packet));
    }

}
