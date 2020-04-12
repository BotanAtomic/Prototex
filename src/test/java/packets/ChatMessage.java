package packets;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.prototex.packet.Packet;
import org.prototex.packet.PacketMapper;
import org.prototex.packet.api.PacketInterface;
import org.prototex.serialization.annotations.JsonMessage;
import org.prototex.session.PrototexSession;

import java.util.concurrent.atomic.AtomicInteger;

@JsonMessage
@Data
@PacketMapper(id = 1)
public class ChatMessage implements PacketInterface {

    private static AtomicInteger integer = new AtomicInteger();

    @SerializedName("name")
    private final String name;

    @SerializedName("message")
    private final String message;

    @Override
    public void handle(PrototexSession session, Packet packet) {
        System.out.println("Receive new ChatMessage " + this);
        try {
            Thread.sleep(1000); //delay simulation
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        session.send(new ChatMessage(name.equals("Client") ? "Server" : "Client", "Message " + integer.incrementAndGet()));
    }

}
