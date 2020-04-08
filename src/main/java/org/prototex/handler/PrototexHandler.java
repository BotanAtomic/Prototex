package org.prototex.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prototex.event.EventManager;
import org.prototex.event.NetworkEvent;
import org.prototex.packet.Packet;
import org.prototex.packet.PacketRegistry;
import org.prototex.session.PrototexSession;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PrototexHandler extends ChannelInboundHandlerAdapter {

    private final static AttributeKey<PrototexSession> ATTR_SESSION = AttributeKey.newInstance("session");

    private final EventManager eventManager;
    private final PacketRegistry packetRegistry;

    private PrototexSession getSession(ChannelHandlerContext ctx) {
        return ctx.channel().attr(ATTR_SESSION).get();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        PrototexSession session = new PrototexSession() {{
            bind(eventManager);
        }};

        ctx.channel().attr(ATTR_SESSION).set(session);

        session.emit(NetworkEvent.REGISTERED, session);
        log.info("Channel {}: registered", ctx.channel().id());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        PrototexSession session = getSession(ctx);
        session.emit(NetworkEvent.UNREGISTERED, session);
        log.info("Channel {}: unregistered", ctx.channel().id());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        PrototexSession session = getSession(ctx);
        session.setActive(ctx.channel());
        log.info("Channel {}: connected", ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        PrototexSession session = getSession(ctx);
        session.emit(NetworkEvent.DISCONNECTED, session);
        log.info("Channel {}: inactive", ctx.channel().id());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PrototexSession session = getSession(ctx);

        session.emit(NetworkEvent.PACKET_RECEIVED, session, msg);
        log.info("Channel {}: read -> {}", ctx.channel().id(), msg);

        Optional.ofNullable(packetRegistry.handle(session, (Packet) msg))
                .ifPresent(object -> session.emit(NetworkEvent.MESSAGE_RECEIVED, session, msg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        PrototexSession session = getSession(ctx);

        if (session.getChannel() != null) {
            log.info("Channel {}: exception", ctx.channel().id(), cause);
            session.emit(NetworkEvent.EXCEPTION, session, cause);
        } else {
            session.emit(NetworkEvent.CONNECTION_FAILED, session, cause);
        }
    }

}
