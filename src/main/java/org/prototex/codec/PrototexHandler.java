package org.prototex.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prototex.event.EventManager;
import org.prototex.event.NetworkEvent;
import org.prototex.session.PrototexSession;

@Slf4j
@RequiredArgsConstructor
public class PrototexHandler implements ChannelInboundHandler {

    private final static AttributeKey<PrototexSession> ATTR_SESSION = AttributeKey.newInstance("session");

    private final EventManager eventManager;

    private PrototexSession getSession(ChannelHandlerContext ctx) {
        return ctx.channel().attr(ATTR_SESSION).get();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(ATTR_SESSION).set(new PrototexSession(ctx.channel().id().asLongText()));

        eventManager.emit(NetworkEvent.REGISTERED, getSession(ctx));
        log.info("Channel {}: registered", ctx.channel().id());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        eventManager.emit(NetworkEvent.UNREGISTERED, getSession(ctx));
        log.info("Channel {}: unregistered", ctx.channel().id());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        eventManager.emit(NetworkEvent.CONNECTED, getSession(ctx));
        log.info("Channel {}: connected", ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        eventManager.emit(NetworkEvent.DISCONNECTED, getSession(ctx));
        log.info("Channel {}: inactive", ctx.channel().id());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Channel {}: read {}", ctx.channel().id(), msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("Channel {}: exception {}", ctx.channel().id(), cause.getMessage());
        eventManager.emit(NetworkEvent.EXCEPTION, getSession(ctx), cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }

}
