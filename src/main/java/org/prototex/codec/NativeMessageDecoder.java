package org.prototex.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.prototex.packet.Packet;
import org.prototex.packet.PacketType;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
public class NativeMessageDecoder extends ByteToMessageDecoder {

    private final ByteArrayOutputStream data = new ByteArrayOutputStream();
    private final int bufferSize;
    private State state;
    private int remainingBytes;
    private int id;
    private Packet packet;

    public NativeMessageDecoder(int bufferSize) {
        this.state = State.INIT;
        this.bufferSize = bufferSize;
    }

    private void decodeId(ByteBuf in) {
        if (in.readableBytes() < Integer.BYTES)
            return;

        id = in.readInt();
        state = State.DECODING_HEADER;
    }

    private void decodeHeader(ByteBuf in) throws Exception {
        if (in.readableBytes() < (Integer.BYTES + Byte.BYTES))
            return;

        PacketType packetType = PacketType.get(in.readByte());

        switch (packetType) {
            case DATA:
                packet = new Packet(id);
                packet.setLength(in.readInt());
                remainingBytes = packet.getLength();
                state = State.DECODING_DATA;
                break;
            case STREAM:
                //TODO: stream wtf
                break;
            case COMMAND:
                //TODO: wtf ???
                break;
            default:
                in.skipBytes(in.readableBytes());
                reset();
                break;
        }
    }

    private void decodeData(ByteBuf in, List<Object> out) throws Exception {
        int bytesToWrite = in.readableBytes();

        if (remainingBytes < bytesToWrite) {
            bytesToWrite = remainingBytes;
        } else if (bytesToWrite < bufferSize && remainingBytes > bytesToWrite) {
            return;
        }

        in.readBytes(data, bytesToWrite);
        remainingBytes -= bytesToWrite;
        if (remainingBytes <= 0) {
            packet.setData(data.toByteArray());
            out.add(packet);
            reset();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!in.isReadable())
            return;

        switch (state) {
            case INIT:
                decodeId(in);
                break;
            case DECODING_HEADER:
                decodeHeader(in);
                break;
            case DECODING_DATA:
                decodeData(in, out);
                break;
        }
    }

    private void reset() {
        state = State.INIT;
        remainingBytes = 0;
        packet = null;
        id = -1;
        data.reset();
    }

    private enum State {
        INIT,
        DECODING_HEADER,
        DECODING_DATA,
        DECODING_STREAM;
    }
}
