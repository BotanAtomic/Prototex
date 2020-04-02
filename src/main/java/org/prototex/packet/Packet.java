package org.prototex.packet;

import lombok.Data;

@Data
public class Packet {

    private final int id;

    private int length;

    private byte[] data;

    @Override
    public String toString() {
        return String.format("Packet(id=%d, length=%d)", id, length);
    }
}
