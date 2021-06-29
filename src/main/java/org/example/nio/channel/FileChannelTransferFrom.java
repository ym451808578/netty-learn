package org.example.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author Castle
 * @Date 2021/6/29 1:36 下午
 */
public class FileChannelTransferFrom {
    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("01.txt")) {
            FileChannel channel1 = fileInputStream.getChannel();
            try (FileOutputStream fileOutputStream = new FileOutputStream("03.txt")) {
                FileChannel channel2 = fileOutputStream.getChannel();
                channel2.transferFrom(channel1, 0, channel1.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
