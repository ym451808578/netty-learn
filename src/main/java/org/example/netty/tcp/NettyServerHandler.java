package org.example.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author Castle
 * @Date 2021/7/6 5:50 上午
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器负责读取的线程：" + Thread.currentThread().getName());
        System.out.println("server ctx:" + ctx.channel().hashCode());
        System.out.println("*****");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = channel.pipeline();
        ByteBuf byteBuf = (ByteBuf) msg;
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                System.out.println("server ctx:" + ctx.channel().hashCode());
                try {
                    Thread.sleep(5*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,这是来自runnable1的消息"+System.currentTimeMillis(),CharsetUtil.UTF_8));
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                System.out.println("server ctx:" + ctx.channel().hashCode());
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,这是来自runnable2的消息"+System.currentTimeMillis(),CharsetUtil.UTF_8));
            }
        });

        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                System.out.println("server ctx:" + ctx.channel().hashCode());
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,这是来自schedule-runnable3的消息"+System.currentTimeMillis(),CharsetUtil.UTF_8));
            }
        },5, TimeUnit.SECONDS);
        System.out.println("服务器接收到的消息："+System.currentTimeMillis() + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + channel.remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端"+System.currentTimeMillis(), CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
