package Prototex.packet;

import com.google.gson.annotations.SerializedName;
import org.prototex.packet.Packet;
import org.prototex.packet.annotations.JsonMessage;
import org.prototex.packet.api.PacketInterface;
import org.prototex.session.PrototexSession;

@JsonMessage
public class ChatMessage extends PacketInterface {

    @SerializedName("name")
    private String name;

    @SerializedName("message")
    private String message;

    public ChatMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public void initialize(PrototexSession session, Packet packet) {
        System.out.println("Initialize ChatMessage");
    }

    @Override
    public void handle(PrototexSession session, Packet packet) {
        System.out.println("Receive " + this);
    }

    @Override
    public String toString() {
        return String.format("ChatMessage(name=%s, message=%s)", name, message);
    }
}
