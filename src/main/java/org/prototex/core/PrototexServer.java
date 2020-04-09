package org.prototex.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.event.EventManager;
import org.prototex.event.NetworkEvent;
import org.prototex.handler.SocketChannelInitializer;
import org.prototex.packet.PacketRegistry;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

@EqualsAndHashCode(callSuper = false)
@Slf4j
@Data
public class PrototexServer extends EventManager {

    private final PrototexConfiguration configuration;

    private final ServerBootstrap serverBootstrap;

    private final PacketRegistry packetRegistry;

    private InetSocketAddress address;

    private ChannelFuture channelFuture;

    private NioEventLoopGroup bossGroup, workerGroup;

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
        bossGroup = new NioEventLoopGroup(configuration.getBossCount());
        workerGroup = new NioEventLoopGroup(configuration.getWorkerCount());

        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new SocketChannelInitializer(this, configuration, packetRegistry));

        if (configuration.getHost() != null && !configuration.getHost().isEmpty()) {
            this.channelFuture = serverBootstrap.bind(configuration.getHost(), configuration.getPort());
        } else {
            this.channelFuture = serverBootstrap.bind(configuration.getPort());
        }

        channelFuture.addListener(future -> {
            emit(NetworkEvent.BOUND, null, null);
            address = ((InetSocketAddress) channelFuture.channel().localAddress());
            log.info("Prototex server started on port {} with {} mapped packets", address.getPort(), packetRegistry.size());
        });
    }

    public CompletableFuture<Boolean> close() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return bossGroup.shutdownGracefully().sync().isSuccess() && workerGroup.shutdownGracefully().sync().isSuccess();
            } catch (InterruptedException | NullPointerException e) {
                return false;
            }
        });
    }

}
