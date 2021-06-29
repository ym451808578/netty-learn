package org.example.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Castle
 * @Date 2021/6/29 9:58 下午
 */
public class NioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("客户端在连接服务器，可以做其他事情");
            }
        }
        String s = "Hello,World";
        ByteBuffer buffer = ByteBuffer.wrap(s.getBytes());
        socketChannel.write(buffer);
        System.in.read();
    }
}
