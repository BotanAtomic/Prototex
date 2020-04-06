package org.prototex.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.event.EventManager;
import org.prototex.event.NetworkEvent;
import org.prototex.handler.SocketChannelInitializer;
import org.prototex.packet.PacketRegistry;
import org.prototex.session.PrototexSession;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@EqualsAndHashCode(callSuper = false)
@Slf4j
@Data
public class PrototexClient extends EventManager {

    private final PrototexConfiguration configuration;

    private final Bootstrap bootstrap;

    private final PacketRegistry packetRegistry;

    private InetSocketAddress address;

    private ChannelFuture channelFuture;

    private PrototexSession session = new PrototexSession();

    private AtomicBoolean initialized = new AtomicBoolean(false);

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

    private void initialize() {
        if (!initialized.getAndSet(true)) {
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(configuration.getWorkerCount());

            bootstrap.group(bossGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new SocketChannelInitializer(this, configuration, packetRegistry));
        }
    }

    public void connect() {
        initialize();

        emit(NetworkEvent.CONNECTING, session);

        channelFuture = bootstrap.connect(configuration.getHost(), configuration.getPort()).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                emit(NetworkEvent.CONNECTION_FAILED, session, future.cause());
                if (configuration.isAutoReconnect())
                    future.channel().eventLoop().schedule(this::connect, configuration.getReconnectionDelay(), TimeUnit.MILLISECONDS);
            } else if (future.isSuccess()) {
                session.setActive(future.channel());
                address = ((InetSocketAddress) channelFuture.channel().localAddress());
                log.info("Prototex client successfully connected to {} on port {}", configuration.getHost(), configuration.getPort());

                if (configuration.isAutoReconnect())
                    on(NetworkEvent.DISCONNECTED, (session1, input) -> connect());
            }
        });

    }

}
