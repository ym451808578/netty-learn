package org.example.nio.buffer;

import java.nio.IntBuffer;

/**
 * @author Castle
 * @Date 2021/6/29 12:26 下午
 */
public class BufferTest {
    public static void main(String[] args) {
        IntBuffer buffer=IntBuffer.allocate(5);
        buffer.put(1);
        buffer.put(5);
        buffer.put(3);
        buffer.put(4);
        System.out.println(buffer.capacity());
        System.out.println(buffer.remaining());
        System.out.println(buffer.limit());
        buffer.flip();
        System.out.println("*******");
        System.out.println(buffer.capacity());
        System.out.println(buffer.remaining());
        System.out.println(buffer.limit());
        System.out.println("*******");
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
        System.out.println("*******");
        System.out.println(buffer.limit());
//        buffer.flip();
//        System.out.println("*******");
//        System.out.println(buffer.capacity());
//        System.out.println(buffer.remaining());
//        System.out.println(buffer.limit());
        buffer.clear();
        System.out.println("*******");
        System.out.println(buffer.capacity());
        System.out.println(buffer.remaining());
        System.out.println(buffer.limit());
//        buffer.flip();
//        System.out.println("*******");
//        System.out.println(buffer.capacity());
//        System.out.println(buffer.remaining());
//        System.out.println(buffer.limit());

    }
}
