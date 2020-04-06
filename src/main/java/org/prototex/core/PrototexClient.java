package org.prototex.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.event.EventManager;
import org.prototex.handler.SocketChannelInitializer;
import org.prototex.packet.PacketRegistry;
import org.prototex.session.PrototexSession;

import java.net.InetSocketAddress;

@EqualsAndHashCode(callSuper = false)
@Slf4j
@Data
public class PrototexClient extends EventManager {

    private final PrototexConfiguration configuration;

    private final Bootstrap bootstrap;

    private final PacketRegistry packetRegistry;

    private InetSocketAddress address;

    private ChannelFuture channelFuture;

    public PrototexClient(PrototexConfiguration configuration, Bootstrap bootstrap) {
        this.configuration = configuration;
        this.bootstrap = bootstrap;
        this.packetRegistry = new PacketRegistry(configuration);
    }

    public PrototexClient(Bootstrap bootstrap) {
        this(PrototexConfiguration.empty(), bootstrap);
    }

    public PrototexClient(PrototexConfiguration configuration) {
        this(configuration, new Bootstrap());
    }

    public PrototexClient() {
        this(PrototexConfiguration.empty(), new Bootstrap());
    }

    public PrototexSession connect() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(configuration.getBossCount());

        bootstrap.group(bossGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new SocketChannelInitializer(this, configuration, packetRegistry));

        this.channelFuture = bootstrap.connect(configuration.getHost(), configuration.getPort()).sync();
        address = ((InetSocketAddress) channelFuture.channel().localAddress());
        log.info("Prototex client successfully connected to {} on port {}", configuration.getHost(), configuration.getPort());
        return new PrototexSession(channelFuture.channel());
    }

}
