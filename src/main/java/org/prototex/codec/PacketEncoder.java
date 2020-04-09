package org.prototex.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prototex.event.NetworkEvent;
import org.prototex.exception.PacketException;
import org.prototex.handler.PrototexHandler;
import org.prototex.packet.Packet;
import org.prototex.packet.PacketRegistry;
import org.prototex.serialization.GenericPacketSerialization;
import org.prototex.session.PrototexSession;
import org.prototex.utils.PacketUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PacketEncoder extends MessageToMessageEncoder<Object> {

    private final PacketRegistry packetRegistry;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        Packet packet;
        PrototexSession session = ctx.channel().attr(PrototexHandler.ATTR_SESSION).get();

        if (msg instanceof Packet)
            packet = (Packet) msg;
        else {
            session.emit(NetworkEvent.MESSAGE_SENT, session, msg);
            Optional<Integer> optionalId = PacketUtils.extractId(msg.getClass());
            if (optionalId.isPresent()) {
                int id = optionalId.get();
                GenericPacketSerialization serializer = packetRegistry.getPackets().get(id);

                if (serializer == null)
                    serializer = packetRegistry.getSerializer(msg.getClass(), null);

                packet = serializer.toPacket(id, msg);
            } else
                throw new PacketException("unable to determine id, class not interfaced by @PacketMapper");
        }

        session.emit(NetworkEvent.PACKET_SENT, session, packet);

        out.add(packet);
    }
}
