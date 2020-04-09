package json;

import config.Configuration;
import org.prototex.core.PrototexServer;
import packets.ChatMessage;

class JsonTestServer {

    public static void main(String[] args) throws Exception {
        PrototexServer server = new PrototexServer(Configuration.configuration);
        server.getPacketRegistry().register(ChatMessage.class);

        server.bind();
        server.getChannelFuture().channel().closeFuture().sync();
    }

}
