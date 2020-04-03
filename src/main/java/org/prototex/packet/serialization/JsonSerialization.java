package org.prototex.packet.serialization;

import com.google.gson.Gson;
import org.prototex.packet.Packet;

public class JsonSerialization extends GenericPacketSerialization {

    public JsonSerialization(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Object build(Packet packet) {
        return new Gson().fromJson(new String(packet.getData()), super.clazz);
    }
}
