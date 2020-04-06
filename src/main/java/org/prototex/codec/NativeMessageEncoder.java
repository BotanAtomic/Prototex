package org.prototex.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prototex.packet.Packet;

@Slf4j
public class NativeMessageEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getId());
        out.writeByte(msg.getType().getId());
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getData());
    }

}
