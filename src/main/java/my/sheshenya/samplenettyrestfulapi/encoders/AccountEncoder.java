package my.sheshenya.samplenettyrestfulapi.encoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import my.sheshenya.samplenettyrestfulapi.model.Account;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AccountEncoder //extends io.netty.handler.codec.serialization.ObjectEncoder
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ByteBufAllocator.DEFAULT
                .buffer()
                .writeBytes(out.toByteArray());
    }



    /**
     * Transforms the ByteBuf to Object
     */
    public static Account fromByteBuf(ByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Account account;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
            MAPPER.setDateFormat(df);
            account = MAPPER.readValue(in, Account.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return account;
    }

}