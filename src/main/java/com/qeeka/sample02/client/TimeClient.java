package com.qeeka.sample02.client;

import com.qeeka.sample02.handler.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by Nathan.Liu on 2020/6/16.
 */
public class TimeClient {

    private final static int port = 8088;

    public void bind() {
        /**
         * 配置客户端NIO线程组
         */
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            /**
             * 发起异步连接操作
             */
            ChannelFuture f = b.connect("localhost", port).sync();

            /**
             * 等待客户端链路关闭
             */
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            /**
             * 释放NIO线程组
             */
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new TimeClient().bind();
    }

}
