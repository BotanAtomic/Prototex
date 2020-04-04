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

    @DoubleValue(4)
    private double doubleTest;

    @LongValue
    private long longTest;

    @ShortValue(2)
    private short shortValue;

    @IntValue(1)
    private int intTest;

    @ByteValue(3)
    private byte byteValue;

    @IntValue(7)
    private int bytesLength;

    @FloatValue(5)
    private float floatTest;

    @BooleanValue(6)
    private boolean booleanTest;

    @BytesValue(value = 8, fieldLength = "bytesLength")
    private String firstMessage;

    @BytesValue(value = 9)
    private String secondTest;

    @Override
    public void handle(PrototexSession session, Packet packet) {
        System.out.println("Receive " + this);
    }


}
