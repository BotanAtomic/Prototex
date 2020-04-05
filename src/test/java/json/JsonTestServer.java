package json;

import com.google.gson.GsonBuilder;
import io.netty.channel.ChannelFuture;
import json.packets.ChatMessage;
import org.junit.jupiter.api.Test;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.core.PrototexServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

class JsonTestServer {

    private final static int PORT = 6999;

    @Test
    public void bindServerTest() throws Exception {
        PrototexServer prototexServer = new PrototexServer(
                PrototexConfiguration.builder()
                        .bufferSize(4096 * 5)
                        .build()
        );

        prototexServer.getPacketRegistry().register(ChatMessage.class);
        ChannelFuture future = prototexServer.bind();

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(prototexServer.getAddress().getPort()));

        if (socket.isConnected()) {
            sendJsonData(1, "Message number 1", socket);
        }

        future.channel().closeFuture().sync();
    }

    private void sendJsonData(int id, String message, Socket socket) throws IOException {
        DataOutputStream stream = new DataOutputStream(socket.getOutputStream());

        String json = new GsonBuilder().create().toJson(new ChatMessage("Botan", message));
        stream.writeInt(id);//id
        stream.writeByte(1);//header data packer type
        stream.writeInt(json.length());//header data packer type
        socket.getOutputStream().write(json.getBytes());
    }

    public String toString() {
        return "JsonTestServer()";
    }
}
