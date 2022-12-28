package com.hk.reactive.netty.demo.c7;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author : HK意境
 * @ClassName : TestRedis
 * @date : 2022/12/27 23:18
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class TestRedis {

    public static final byte[] LINE = {13, 10};

    public static void main(String[] args) throws InterruptedException {

        /**
         * set name zhangsan
         * *3
         * $3
         * set
         * $4
         * name
         * $8
         * zhangsan
         */


        // NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ByteBuf buffer = ctx.alloc().buffer();
                                sendCommand(buffer);
                                ctx.writeAndFlush(buffer);
                                super.channelActive(ctx);
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // msg 是 ByteBuf
                                ByteBuf buf = (ByteBuf) msg;
                                log.info("响应消息：{}", buf.toString(Charset.defaultCharset()));

                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect("localhost", 6379).sync();
        channelFuture.channel().closeFuture().sync();


    }

    private static void sendCommand(ByteBuf buffer) {
        buffer.writeBytes("*3".getBytes(StandardCharsets.UTF_8));
        buffer.writeBytes(LINE);
        buffer.writeBytes("$3".getBytes(StandardCharsets.UTF_8));
        buffer.writeBytes(LINE);
        buffer.writeBytes("set".getBytes(StandardCharsets.UTF_8));
        buffer.writeBytes(LINE);
        buffer.writeBytes("$4".getBytes(StandardCharsets.UTF_8));
        buffer.writeBytes(LINE);
        buffer.writeBytes("name".getBytes(StandardCharsets.UTF_8));
        buffer.writeBytes(LINE);
        buffer.writeBytes("$8".getBytes(StandardCharsets.UTF_8));
        buffer.writeBytes(LINE);
        buffer.writeBytes("zhangsan".getBytes(StandardCharsets.UTF_8));
        buffer.writeBytes(LINE);
    }

}
