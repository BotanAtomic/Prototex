package org.prototex.packet;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "data")
public class Packet {

    private final int id;

    private final PacketType type;

    private int length;

    private byte[] data;

    public static Packet dataPacket(int id, byte[] data) {
        return new Packet(id, PacketType.DATA) {{
            setLength(data.length);
            setData(data);
        }};
    }

}
