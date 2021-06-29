package org.example.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Castle
 * @Date 2021/6/29 10:59 下午
 */
public class ChatServer {
    private static int PORT = 6667;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public ChatServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer();
        chatServer.listen();
    }

    public void listen() throws IOException {
        while (true) {
            int num = selector.select();
            if (num > 0) {
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("客户端上线：" + socketChannel.getRemoteAddress());
                    }
                    if (selectionKey.isReadable()) {
                        readData(selectionKey);
                    }
                    iterator.remove();
                }
            } else {
                System.out.println("没有连接");
            }
        }
    }

    private void readData(SelectionKey selectionKey) {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int count = 0;
        try {
            count = channel.read(buffer);
        } catch (IOException exception) {
            exception.printStackTrace();
            try {
                System.out.println("客户端离线了：" + channel.getRemoteAddress());
                selectionKey.cancel();
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (count > 0) {
            String msg = new String(Arrays.copyOf(buffer.array(), count));
            System.out.println("收到来自客户端的信息：" + msg);
            sentMsgToOtherClient(msg, channel);
        }
    }

    private void sentMsgToOtherClient(String msg, SocketChannel channel) {
        System.out.println("服务器转发消息");
        for (SelectionKey selectionKey : selector.keys()) {
            Channel targetChannel = selectionKey.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != channel) {
                SocketChannel target = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                try {
                    target.write(buffer);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

    }
}
