package Prototex.packet;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.prototex.packet.Packet;
import org.prototex.packet.api.PacketInterface;
import org.prototex.serialization.annotations.PacketMapper;
import org.prototex.serialization.annotations.message.JsonMessage;
import org.prototex.session.PrototexSession;

@JsonMessage
@Data
@PacketMapper(id = 1)
public class ChatMessage implements PacketInterface {

    @SerializedName("name")
    private final String name;

    @SerializedName("message")
    private final String message;

    @Override
    public void handle(PrototexSession session, Packet packet) {
        System.out.println("Receive " + this);
    }

}
