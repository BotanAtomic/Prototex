package org.prototex.packet.api;

import org.prototex.packet.Packet;
import org.prototex.session.PrototexSession;

public interface PacketInterface {

    void handle(PrototexSession session, Packet packet);

}
