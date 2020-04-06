package org.prototex.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prototex.exception.PacketException;
import org.prototex.packet.Packet;
import org.prototex.packet.PacketRegistry;
import org.prototex.serialization.GenericPacketSerialization;
import org.prototex.utils.PacketUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PacketEncoder extends MessageToMessageEncoder<Object> {

    private final PacketRegistry packetRegistry;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        if (msg instanceof Packet)
            out.add(msg);
        else {
            int id = PacketUtils.extractId(msg.getClass());

            if (id != -1) {
                GenericPacketSerialization serializer = packetRegistry.getPackets().get(id);

                if (serializer == null)
                    serializer = packetRegistry.getSerializer(msg.getClass(), null);

                out.add(serializer.toPacket(id, msg));
            } else
                throw new PacketException("unable to determine id, class not interfaced by @PacketMapper");
        }
    }
}
