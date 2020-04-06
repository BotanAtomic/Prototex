package org.prototex.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.event.EventManager;
import org.prototex.event.NetworkEvent;
import org.prototex.handler.SocketChannelInitializer;
import org.prototex.packet.PacketRegistry;

import java.net.InetSocketAddress;

@EqualsAndHashCode(callSuper = false)
@Slf4j
@Data
public class PrototexServer extends EventManager {

    private final PrototexConfiguration configuration;

    private final ServerBootstrap serverBootstrap;

    private final PacketRegistry packetRegistry;

    private InetSocketAddress address;

    private ChannelFuture channelFuture;

    public PrototexServer(PrototexConfiguration configuration, ServerBootstrap serverBootstrap) {
        this.configuration = configuration;
        this.serverBootstrap = serverBootstrap;
        this.packetRegistry = new PacketRegistry(configuration);
    }

    public PrototexServer(ServerBootstrap serverBootstrap) {
        this(PrototexConfiguration.empty(), serverBootstrap);
    }

    public PrototexServer(PrototexConfiguration configuration) {
        this(configuration, new ServerBootstrap());
    }

    public PrototexServer() {
        this(PrototexConfiguration.empty(), new ServerBootstrap());
    }

    public void bind() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(configuration.getBossCount());
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(configuration.getWorkerCount());

        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new SocketChannelInitializer(this, configuration, packetRegistry));

        if (configuration.getHost() != null && !configuration.getHost().isEmpty()) {
            this.channelFuture = serverBootstrap.bind(configuration.getHost(), configuration.getPort()).sync();
        } else {
            this.channelFuture = serverBootstrap.bind(configuration.getPort()).sync();
        }

        emit(NetworkEvent.BOUND, null, null);
        address = ((InetSocketAddress) channelFuture.channel().localAddress());
        log.info("Prototex server started on port {} with {} mapped packets", address.getPort(), packetRegistry.size());
    }

}
