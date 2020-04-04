package Prototex.packet;

import lombok.Data;
import org.prototex.packet.Packet;
import org.prototex.packet.api.PacketInterface;
import org.prototex.serialization.annotations.PacketMapper;
import org.prototex.serialization.annotations.message.BinaryMessage;
import org.prototex.serialization.annotations.value.*;
import org.prototex.session.PrototexSession;

@BinaryMessage
@Data
@PacketMapper(id = 2)
public class BinaryTestMessage implements PacketInterface {

    @DoubleValue(index = 4)
    private double doubleTest;

    @LongValue
    private long longTest;

    @ShortValue(index = 2)
    private short shortValue;

    @IntValue(index = 1)
    private int intTest;

    @ByteValue(index = 3)
    private byte byteValue;

    @IntValue(index = 7)
    private int bytesLength;

    @FloatValue(index = 5)
    private float floatTest;

    @BooleanValue(index = 6)
    private boolean booleanTest;

    @BytesValue(index = 8, fieldLength = "bytesLength")
    private String firstMessage;

    @BytesValue(index = 9)
    private String secondTest;

    @Override
    public void handle(PrototexSession session, Packet packet) {
        System.out.println("Receive " + this);
    }


}
