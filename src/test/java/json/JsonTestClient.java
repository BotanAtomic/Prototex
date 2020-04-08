package json;

import config.Configuration;
import org.prototex.core.PrototexClient;
import org.prototex.event.NetworkEvent;
import packets.ChatMessage;

class JsonTestClient {


    public static void main(String[] args) throws Exception {
        PrototexClient client = new PrototexClient(Configuration.configuration);
        client.getPacketRegistry().register(ChatMessage.class);

        client.on(NetworkEvent.CONNECTING, (session, input) -> System.out.println("Connecting to server..."));

        client.on(NetworkEvent.DISCONNECTED, (session, input) -> System.out.println("Disconnected to server"));

        client.on(NetworkEvent.CONNECTION_FAILED, (session, input) -> System.out.println("Connection failed " + ((Exception) input).getMessage()));

        client.on(NetworkEvent.CONNECTED, (session, input) -> {
            System.out.println("Connected !");
            client.getSession().send(new ChatMessage("Client", "First message")).addListener(future -> System.out.println("First message sent !"));
        });

        client.connect();

        client.getChannelFuture().channel().closeFuture().sync();
    }

}
