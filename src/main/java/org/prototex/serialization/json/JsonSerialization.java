package org.prototex.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.exception.SerializationException;
import org.prototex.packet.Packet;
import org.prototex.serialization.GenericPacketSerialization;

public class JsonSerialization extends GenericPacketSerialization {

    private final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    private final PrototexConfiguration configuration;

    public JsonSerialization(Class<?> clazz, PrototexConfiguration configuration) {
        super(clazz);
        this.configuration = configuration;
    }

    @Override
    public Packet toPacket(int id, Object object) throws Exception {
        try {
            return Packet.dataPacket(id, gson.toJson(object).getBytes(configuration.getCharset()));
        } catch (JsonSyntaxException e) {
            throw new SerializationException(String.format("cannot convert %s to json output", object.getClass()));
        }
    }

    @Override
    public Object fromPacket(Packet packet) throws Exception {
        try {
            return gson.fromJson(new String(packet.getData(), configuration.getCharset()), super.clazz);
        } catch (JsonSyntaxException e) {
            throw new SerializationException("invalid json input");
        }
    }
}
