package org.prototex.exception;

import org.prototex.packet.Packet;

public class UnknownPacketException extends PrototexException {

    public UnknownPacketException(Packet packet) {
        super(String.format("unknown incoming packet %s", packet));
    }

}
