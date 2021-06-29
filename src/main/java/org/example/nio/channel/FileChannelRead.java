package org.example.nio.channel;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author Castle
 * @Date 2021/6/29 1:09 下午
 */
public class FileChannelRead {
    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("01.txt")) {
            FileChannel channel = fileInputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(5);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                buffer.clear();
                int read = channel.read(buffer);
                if (read == -1) {
                    break;
                }
                buffer.flip();
                stringBuilder.append(new String(Arrays.copyOf(buffer.array(), read)));
            }
            System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
