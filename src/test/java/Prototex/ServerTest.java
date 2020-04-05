package Prototex;

import Prototex.packet.ChatMessage;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.core.PrototexServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

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

        prototexServer.getPacketRegistry().registerPackage(ChatMessage.class.getPackage().getName());

        prototexServer.bind(false);
        assertNotNull(prototexServer.getChannelFuture(), "channelHandler must not be null");

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(6999));

        if (socket.isConnected()) {
            sendJsonData(1, "Message number 1", socket);
        }

        while (socket.isConnected()) ;
    }

    private void sendJsonData(int id, String message, Socket socket) throws IOException {
        DataOutputStream stream = new DataOutputStream(socket.getOutputStream());

        String json = new GsonBuilder().create().toJson(new ChatMessage("Botan", message));
        stream.writeInt(id);//id
        stream.writeByte(1);//header data packer type
        stream.writeInt(json.length());//header data packer type
        socket.getOutputStream().write(json.getBytes());
    }

}
