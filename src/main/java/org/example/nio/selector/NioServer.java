package org.example.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Castle
 * @Date 2021/6/29 9:07 下午
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(6666));
        Selector selector = Selector.open();
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //循环等待客户端
        while (true) {
            if (selector.select(2000) == 0) {
                System.out.println("等待2秒，无连接，可以做其他事情");
                continue;
            }
            //获取所有的selectionKey的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //如果是连接事件
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功。生成的socketChannel：" + socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    //将socketChannel注册到selector，关注事件为OP_READ，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //如果是读取事件
                if (selectionKey.isReadable()) {
                    //通过selectionKey获取channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //通过selectionKey获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(buffer);
                    System.out.println("读取来自客户端的数据：" + new String(Arrays.copyOf(buffer.array(),buffer.position())));
                }
                //从集合中移除这个selectionKey，防止重复操作
                iterator.remove();
            }
        }
    }
}
