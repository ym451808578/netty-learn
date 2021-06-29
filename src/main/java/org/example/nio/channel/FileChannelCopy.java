package org.example.nio.channel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Castle
 * @Date 2021/6/29 12:45 下午
 */
public class FileChannelCopy {
    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("01.txt")) {
            FileChannel channel = fileInputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(5);
            int read = channel.read(buffer);
            try (FileOutputStream fileOutputStream = new FileOutputStream("02.txt")) {
                while (read != -1) {
                    buffer.flip();
                    FileChannel outputStreamChannel = fileOutputStream.getChannel();
                    outputStreamChannel.write(buffer);
                    buffer.clear();
                    read = channel.read(buffer);

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
