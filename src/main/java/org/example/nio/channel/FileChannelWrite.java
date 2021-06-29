package org.example.nio.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Castle
 * @Date 2021/6/29 12:40 下午
 */
public class FileChannelWrite {
    public static void main(String[] args) {
        String s="Hello World!";
        try (FileOutputStream fileOutputStream=new FileOutputStream("01.txt")){
            FileChannel channel = fileOutputStream.getChannel();
            ByteBuffer buffer=ByteBuffer.allocate(s.length());
            buffer.put(s.getBytes());
            buffer.flip();
            channel.write(buffer);

        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
