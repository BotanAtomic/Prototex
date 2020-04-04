package org.prototex.exception;

import org.prototex.packet.Packet;

public class InvalidPacketException extends PrototexException {

    public InvalidPacketException(Packet packet) {
        super(String.format("invalid incoming packet %s", packet));
    }

}
