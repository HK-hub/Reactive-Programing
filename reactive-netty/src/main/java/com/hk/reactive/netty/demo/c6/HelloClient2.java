package com.hk.reactive.netty.demo.c6;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Random;

/**
 * @author : HK意境
 * @ClassName : HelloClient2
 * @date : 2022/12/26 22:57
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class HelloClient2 {

    private static final int fixedLength = 10;

    public static byte[] fillBytes(char c, int len) {

        byte[] bytes = new byte[fixedLength];
        int size = Math.min(len, fixedLength);

        int count = 0;
        for (; count < size; count++) {
            bytes[count] = (byte) c;
        }

        for ( ; count < bytes.length ; ++count) {
            bytes[count] = '_';
        }
        for (byte aByte : bytes) {
            System.out.print((char) aByte);
        }
        System.out.println();
        return bytes;
    }


    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                // 会在 channel 连接建立成功后，触发 active 事件
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ByteBuf buffer = ctx.alloc().buffer();
                                    char c = '0';
                                    Random random = new Random();
                                    for (int i = 0; i < 10; i++) {
                                        byte[] bytes = fillBytes(c, random.nextInt(10) + 1);
                                        c++;
                                        buffer.writeBytes(bytes);
                                    }
                                    ctx.writeAndFlush(buffer);
                                    super.channelInactive(ctx);
                                }
                            });
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 8080).sync();
            channelFuture.channel().closeFuture().sync();
        }catch(Exception e){
            e.printStackTrace();

        }finally {
            worker.shutdownGracefully();
        }
    }


}
