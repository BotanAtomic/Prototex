package org.prototex.packet;

import lombok.Getter;

public enum PacketType {
    COMMAND(0),
    DATA(1),
    STREAM(2),
    ERROR(-1);

    @Getter
    private final byte id;

    PacketType(int id) {
        this.id = (byte) id;
    }

    public static PacketType get(byte id) {
        for (PacketType value : values())
            if (value.id == id)
                return value;
        return ERROR;
    }
}
