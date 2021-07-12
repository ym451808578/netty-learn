package org.example.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @author Castle
 * @Date 2021/7/12 9:43 下午
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * added表示链接建立，一旦连接，第一个被执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("handlerAdded:" + "[客户端]" + channel.remoteAddress() + "加入聊天" + LocalDateTime.now());
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天" + LocalDateTime.now());
        channelGroup.add(channel);
    }

    /**
     * 断开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("handlerRemoved:" + "[客户端]" + channel.remoteAddress() + "离开了" + LocalDateTime.now());
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了" + LocalDateTime.now());
        System.out.println("在线的用户数量：" + channelGroup.size());
    }

    /**
     * channel处于活跃状态
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive:" + ctx.channel().remoteAddress() + "上线了");
    }


    /**
     * channel不再活跃
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive:" + ctx.channel().remoteAddress() + "下线了");
    }

    /**
     * 处理idleStateEvent
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String type = null;
            switch (event.state()) {
                case READER_IDLE:
                    type = "读空闲";
                    break;
                case WRITER_IDLE:
                    type = "写空闲";
                    break;
                case ALL_IDLE:
                    type = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "超时时间：" + type);
            System.out.println("服务器可以做相应处理了");
        }
    }

    /**
     * 读取数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (ch != channel) {
                ch.writeAndFlush("[客户端]" + channel.remoteAddress() + "发送了消息：" + msg);
            } else {
                ch.writeAndFlush("[自己]发送了消息：" + msg);
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }
}
