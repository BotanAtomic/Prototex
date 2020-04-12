package json;

import config.Configuration;
import org.prototex.core.PrototexClient;
import org.prototex.event.NetworkEvent;
import org.prototex.packet.Packet;
import packets.ChatMessage;

class JsonTestClient {


    public static void main(String[] args) throws Exception {
        PrototexClient client = new PrototexClient(Configuration.configuration);
        client.getPacketRegistry().register(ChatMessage.class);

        client.on(NetworkEvent.CONNECTING, (session, input) -> System.out.println("Connecting to server..."));

        client.on(NetworkEvent.DISCONNECTED, (session, input) -> System.out.println("Disconnected to server"));

        client.on(NetworkEvent.CONNECTION_FAILED, (session, input) -> System.out.println("Connection failed " + ((Exception) input).getMessage()));

        client.on(NetworkEvent.CONNECTED, (session, input) -> {
            System.out.println("Connected to server !");
            session.send(new ChatMessage("Client", "First message")).addListener(future -> System.out.println("First message sent !"));
            session.send(Packet.data(1, "{ \"name\": \"Client\",\"message\": \"Second message\"  }".getBytes()));
        });

        client.on(NetworkEvent.MESSAGE_SENT, (session, input) -> System.out.println("Send " + input.getClass()));

        client.connect();
        client.getChannelFuture().channel().closeFuture().sync();
    }

}
