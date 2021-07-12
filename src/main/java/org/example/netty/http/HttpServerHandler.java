package org.example.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author Castle
 * @Date 2021/7/6 10:30 下午
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpMessage msg) throws Exception {
        System.out.println(msg.getClass());
        if (msg instanceof HttpRequest) {
            System.out.println("pipeline hashcode:" + channelHandlerContext.channel().pipeline().hashCode());
            System.out.println("ChannelHandlerContext hashcode:" + this.hashCode());
            System.out.println("msg类型：" + msg.getClass());
            System.out.println("客户端地址：" + channelHandlerContext.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            System.out.println(uri);
            if ("/favicon.ico".equalsIgnoreCase(uri.getPath())) {
                System.out.println("最爱图标地址，忽略");
                return;
            }
            System.out.println("开始返回消息");
            ByteBuf content = Unpooled.copiedBuffer("hello，这里是服务器",CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
