package org.prototex.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prototex.codec.NativeMessageDecoder;
import org.prototex.codec.NativeMessageEncoder;
import org.prototex.codec.PacketEncoder;
import org.prototex.configuration.PrototexConfiguration;
import org.prototex.event.EventManager;
import org.prototex.packet.PacketRegistry;

@Slf4j
@RequiredArgsConstructor
public class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final EventManager eventManager;

    private final PrototexConfiguration configuration;

    private final PacketRegistry packetRegistry;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new NativeMessageDecoder(configuration.getBufferSize()));
        pipeline.addLast(new NativeMessageEncoder());
        pipeline.addLast(new PacketEncoder(packetRegistry));
        pipeline.addLast(new PrototexHandler(eventManager, packetRegistry));
    }
}