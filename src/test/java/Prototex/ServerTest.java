package Prototex;

import org.junit.jupiter.api.Test;
import org.prototex.event.NetworkEvent;
import org.prototex.server.core.PrototexServer;
import org.prototex.server.core.PrototexServerConfiguration;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ServerTest {

    @Test
    public void bindServerTest() throws Exception {
        PrototexServer prototexServer = new PrototexServer(
                PrototexServerConfiguration.builder()
                        .port(6999)
                        .bufferSize(4096 * 5)
                        .build()
        );

        prototexServer.on(NetworkEvent.CONNECTED, (session, input) -> {
            System.out.println("New connection " + session.getId());
        });

        prototexServer.bind(false);
        assertNotNull(prototexServer.getChannelFuture(), "channelHandler must not be null");

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(6999));

        if (socket.isConnected()) {
            Random random = new Random();
            sendData(random.nextInt(), 9000000, socket);
            sendData(random.nextInt(), 12, socket);
        }


        while (socket.isConnected()) ;
    }

    private void sendData(int id, int length, Socket socket) throws IOException {
        DataOutputStream stream = new DataOutputStream(socket.getOutputStream());

        byte[] bytes = new byte[length];

        stream.writeInt(id);//id
        stream.writeInt(1);//header data packer type
        stream.writeInt(bytes.length);//header data packer type
        socket.getOutputStream().write(bytes);
    }


}
