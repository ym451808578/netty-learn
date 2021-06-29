package org.example.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author Castle
 * @Date 2021/6/29 11:32 下午
 */
public class ChatClient {
    private static final int PORT = 6667;
    private static final String HOST = "127.0.0.1";
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public ChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("username:" + username);
    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        chatClient.receiveMsg();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            chatClient.sentMsg(msg);
        }
    }

    public void sentMsg(String msg) throws IOException {
        msg = username + " 说：" + msg;
        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
    }

    public void receiveMsg() throws IOException {
        int count = selector.select();
        if (count > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = channel.read(buffer);
                    String msg = new String(Arrays.copyOf(buffer.array(), read));
                    System.out.println("收到消息：" + msg.trim());
                }
                iterator.remove();
            }
        }
    }
}
