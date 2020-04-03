package org.prototex.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.event.EventManager;
import org.prototex.handler.SocketChannelInitializer;
import org.prototex.packet.PacketRegistry;

import java.net.InetSocketAddress;

@EqualsAndHashCode(callSuper = false)
@Slf4j
@Data
public class PrototexServer extends EventManager {

    private final PrototexConfiguration configuration;

    private final ServerBootstrap serverBootstrap;

    private final PacketRegistry packetRegistry = new PacketRegistry();

    private ChannelFuture channelFuture;

        public PrototexServer(PrototexConfiguration configuration, ServerBootstrap serverBootstrap) {
        this.configuration = configuration;
        this.serverBootstrap = serverBootstrap;
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

    public void bind() {
        this.bind(true);
    }

    public void bind(boolean sync) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(configuration.getBossCount());
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(configuration.getWorkerCount());

        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new SocketChannelInitializer(this, configuration, packetRegistry));

        try {
            if (configuration.getHost() != null && !configuration.getHost().isEmpty()) {
                this.channelFuture = serverBootstrap.bind(configuration.getHost(), configuration.getPort()).sync();
            } else {
                this.channelFuture = serverBootstrap.bind(configuration.getPort()).sync();
            }

            log.info("Prototex server started on port {}", ((InetSocketAddress) channelFuture.channel().localAddress()).getPort());

            if (sync)
                channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("Prototex server stopped: {}", e.getMessage());
        }
    }

}
