package json;

import json.packets.ChatMessage;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.core.PrototexClient;
import org.prototex.core.PrototexServer;
import org.prototex.session.PrototexSession;

class JsonTestServer {

    public static void main(String[] args) throws Exception {
        PrototexServer server = buildServer();
        PrototexSession client = buildClient(server.getAddress().getPort());

        client.send(new ChatMessage("Client", "First message")).addListener(future -> System.out.println("First message sent !"));

        server.getChannelFuture().channel().closeFuture().sync();
    }

    private static PrototexServer buildServer() throws Exception {
        PrototexServer server = new PrototexServer(
                PrototexConfiguration.builder()
                        .bufferSize(4096 * 5)
                        .build()
        );

        server.getPacketRegistry().register(ChatMessage.class);

        server.bind();
        return server;
    }

    private static PrototexSession buildClient(int port) throws Exception {
        PrototexClient client = new PrototexClient(
                PrototexConfiguration.builder()
                        .bufferSize(4096 * 5)
                        .port(port)
                        .build()
        );

        client.getPacketRegistry().register(ChatMessage.class);

        return client.connect();
    }

}
