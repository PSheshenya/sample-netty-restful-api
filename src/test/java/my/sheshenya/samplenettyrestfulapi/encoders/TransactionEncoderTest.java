package my.sheshenya.samplenettyrestfulapi.encoders;

import io.netty.buffer.ByteBuf;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionEncoderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void toByteBuf() {
        Transaction t1 = new Transaction("a1", "a2", 1000);
        ByteBuf buf = TransactionEncoder.toByteBuf(t1);

        Assert.assertNotNull(buf);
    }

    @Test
    public void fromByteBuf() {
        Transaction t1 = new Transaction("a1", "a2", 1000);
        ByteBuf buf = TransactionEncoder.toByteBuf(t1);

        Assert.assertNotNull(buf);

        Transaction t2 = TransactionEncoder.fromByteBuf(buf);
        Assert.assertNotNull(t2);

        Assert.assertEquals(TransactionEncoder.getJsonString(t1), TransactionEncoder.getJsonString(t2));

    }
}