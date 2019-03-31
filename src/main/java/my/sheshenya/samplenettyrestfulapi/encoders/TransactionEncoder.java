package my.sheshenya.samplenettyrestfulapi.encoders;

import com.fasterxml.jackson.core.JsonProcessingException;
import my.sheshenya.samplenettyrestfulapi.model.Transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TransactionEncoder //extends io.netty.handler.codec.serialization.ObjectEncoder
{

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Transforms the Object to ByteBuf
     */
    public static ByteBuf toByteBuf(Object any) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
            MAPPER.setDateFormat(df);
            MAPPER.writeValue(out, any);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ByteBufAllocator.DEFAULT
                .buffer()
                .writeBytes(out.toByteArray());
    }



    /**
     * Transforms the ByteBuf to Object
     */
    public static Transaction fromByteBuf(ByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Transaction transaction;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
            MAPPER.setDateFormat(df);
            transaction = MAPPER.readValue(in, Transaction.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return transaction;
    }

    public static String getJsonString(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}