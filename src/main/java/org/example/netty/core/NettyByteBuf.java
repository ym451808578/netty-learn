package org.example.netty.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author Castle
 * @Date 2021/7/12 9:21 下午
 */
public class NettyByteBuf {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }

        System.out.println("capacity:" + byteBuf.capacity());
        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println(byteBuf.readByte());
        }

        ByteBuf buf = Unpooled.copiedBuffer("Hello,JAVA!", Charset.forName("UTF-8"));
        if (buf.hasArray()) {
            byte[] array = buf.array();
            System.out.println(new String(array, Charset.forName("UTF-8")));
            System.out.println(buf.arrayOffset());
            System.out.println(buf.readerIndex());
            System.out.println(buf.writerIndex());
            System.out.println(buf.capacity());
            System.out.println(buf.getByte(0));
            int length = buf.readableBytes();
            System.out.println("length of readableBytes:" + length);
            for (int i = 0; i < length; i++) {
                System.out.println((char) buf.getByte(i));
                System.out.println(buf.readByte());
            }
            System.out.println(buf.arrayOffset());
            System.out.println(buf.readerIndex());
            System.out.println(buf.writerIndex());
            System.out.println(buf.capacity());
        }

    }
}
