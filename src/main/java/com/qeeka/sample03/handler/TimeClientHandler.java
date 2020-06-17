package com.qeeka.sample03.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Nathan.Liu on 2020/6/16.
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    ByteBuf firstMessage = null;
    private byte[] req;
    private int count;

    public TimeClientHandler() {
        req = ("QUERY TIME ORDER . $_").getBytes();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与服务器端链接建立成功...");
        ByteBuf message = null;
        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String body = (String) msg;
        System.out.println(String.format("服务器返回时间:%s , 计数器:%d", body, ++count));


    }
}
