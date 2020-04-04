package org.prototex.stream;

import org.prototex.serialization.annotations.value.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BinaryInputStream {

    private final DataInputStream inputStream;

    public BinaryInputStream(byte[] data) {
        this.inputStream = new DataInputStream(new ByteArrayInputStream(data));
    }

    @ByteValue
    public byte readByte() throws IOException {
        return inputStream.readByte();
    }

    @ShortValue
    public short readShort() throws IOException {
        return inputStream.readShort();
    }

    @IntValue
    public int readInt() throws IOException {
        return inputStream.readInt();
    }

    @LongValue
    public long readLong() throws IOException {
        return inputStream.readLong();
    }

    @FloatValue
    public float readFloat() throws IOException {
        return inputStream.readFloat();
    }

    @DoubleValue
    public double readDouble() throws IOException {
        return inputStream.readDouble();
    }

    @BooleanValue
    public boolean readBoolean() throws IOException {
        return inputStream.readBoolean();
    }

    @BytesValue
    public byte[] readBytes(int length) throws IOException {
        length = length == 0 ? inputStream.available() : length;

        if (length < 0)
            return null;

        byte[] bytes = new byte[length];

        if (inputStream.available() < length)
            throw new IOException(String.format("cannot read %d bytes, available: %d", length, inputStream.available()));

        inputStream.readFully(bytes);
        return bytes;
    }

}
