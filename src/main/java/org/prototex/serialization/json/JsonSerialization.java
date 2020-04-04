package org.prototex.serialization.json;

import com.google.gson.Gson;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.packet.Packet;
import org.prototex.serialization.GenericPacketSerialization;

public class JsonSerialization extends GenericPacketSerialization {

    private final PrototexConfiguration configuration;

    public JsonSerialization(Class<?> clazz, PrototexConfiguration configuration) {
        super(clazz);
        this.configuration = configuration;
    }

    @Override
    public Packet toPacket(int id, Object object) throws Exception {
        Packet packet = new Packet(id);

        byte[] data = new Gson().toJson(object).getBytes(configuration.getCharset());
        packet.setLength(data.length);
        packet.setData(data);

        return packet;
    }

    @Override
    public Object fromPacket(Packet packet) throws Exception {
        return new Gson().fromJson(new String(packet.getData(), configuration.getCharset()), super.clazz);
    }
}
