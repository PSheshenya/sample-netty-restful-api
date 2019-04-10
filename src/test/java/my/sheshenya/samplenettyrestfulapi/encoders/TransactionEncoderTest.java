package my.sheshenya.samplenettyrestfulapi.encoders;

import io.netty.buffer.ByteBuf;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class TransactionEncoderTest {

    @Test
    public void toByteBuf() {
        Transaction t1 = new Transaction("a1", "a2", BigDecimal.valueOf(1000));
        ByteBuf buf = TransactionEncoder.toByteBuf(t1);

        Assert.assertNotNull(buf);
    }

    @Test
    public void fromByteBuf() {
        Transaction t1 = new Transaction("a1", "a2", BigDecimal.valueOf(1000));
        ByteBuf buf = TransactionEncoder.toByteBuf(t1);

        Assert.assertNotNull(buf);

        Transaction t2 = TransactionEncoder.fromByteBuf(buf);
        Assert.assertNotNull(t2);

        Assert.assertEquals(TransactionEncoder.getJsonString(t1), TransactionEncoder.getJsonString(t2));

    }
}