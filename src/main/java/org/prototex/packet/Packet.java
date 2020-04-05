package org.prototex.packet;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "data")
public class Packet {

    private final int id;

    private int length;

    private byte[] data;

}
