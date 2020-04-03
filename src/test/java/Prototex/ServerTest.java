package Prototex;

import Prototex.packet.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.event.NetworkEvent;
import org.prototex.server.PrototexServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ServerTest {

    @Test
    public void bindServerTest() throws Exception {
        PrototexServer prototexServer = new PrototexServer(
                PrototexConfiguration.builder()
                        .port(6999)
                        .bufferSize(4096 * 5)
                        .build()
        );

        prototexServer.getPacketRegistry().register(1, ChatMessage.class);

        prototexServer.on(NetworkEvent.PACKET_RECEIVED, (session, input) -> System.out.println("New incoming packet " + input));
        prototexServer.on(NetworkEvent.CONNECTED, (session, input) -> System.out.println("New connection, session " + session.getId()));

        prototexServer.bind(false);
        assertNotNull(prototexServer.getChannelFuture(), "channelHandler must not be null");

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(6999));

        if (socket.isConnected()) {
            sendData(socket);
        }

        while (socket.isConnected()) ;
    }

    private void sendData(Socket socket) throws IOException {
        DataOutputStream stream = new DataOutputStream(socket.getOutputStream());

        ChatMessage message = new ChatMessage("Hello", "Botan");

        String json = new GsonBuilder().create().toJson(message);

        System.out.println(json);

        stream.writeInt(1);//id
        stream.writeByte(1);//header data packer type
        stream.writeInt(json.length());//header data packer type
        socket.getOutputStream().write(json.getBytes());
    }


}
