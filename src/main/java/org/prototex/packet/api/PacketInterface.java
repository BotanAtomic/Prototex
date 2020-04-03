package org.prototex.packet.api;

import org.prototex.packet.Packet;
import org.prototex.session.PrototexSession;

public abstract class PacketInterface {

    public void initialize(PrototexSession session, Packet packet) {
    }

    public void handle(PrototexSession session, Packet packet) {
    }

}
