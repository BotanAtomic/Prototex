package org.prototex.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.prototex.codec.NativeMessageDecoder;
import org.prototex.event.EventManager;

@Slf4j
public class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final EventManager eventManager;

    private final int bufferSize;

    public SocketChannelInitializer(EventManager eventManager, int bufferSize) {
        this.eventManager = eventManager;
        this.bufferSize = bufferSize;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new NativeMessageDecoder(bufferSize));
        pipeline.addLast(new PrototexHandler(eventManager));
    }
}