package org.prototex.packet;

public class DataPacket extends Packet {

    private byte[] data;

    public DataPacket(int id) {
        super(id);
    }

    public void write(byte[] bytes) {

    }
}

