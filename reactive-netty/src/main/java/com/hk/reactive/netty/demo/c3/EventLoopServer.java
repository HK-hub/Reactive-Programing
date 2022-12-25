package com.hk.reactive.netty.demo.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author : HK意境
 * @ClassName : EventLoopServer
 * @date : 2022/12/25 21:56
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class EventLoopServer {

    public static void main(String[] args) {

        // 细分2：创建一个独立的 EventLoopGroup
        DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();

        new ServerBootstrap()
                // boss 和 worker
                // 细分1：boss 只负责ServerSocketChannel 上的 accept 事件，worker 只负责 socketChannel上的读写事件

                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        // 连接建立的时候会执行 initializer 处理工序
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                // socketChannel.pipeline().addLast(new StringDecoder());
                                socketChannel.pipeline()
                                        .addLast("handler1", new ChannelInboundHandlerAdapter() {
                                    // 没有了编解码器，这里的 msg 就是 ByteBuf 类型
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buffer = (ByteBuf) msg;
                                        log.info("handler1 received:{}", buffer.toString(StandardCharsets.UTF_8));

                                        // 让消息传递给下一个handler
                                        ctx.fireChannelRead(msg);
                                    }
                                })
                                        .addLast(defaultEventLoopGroup, "handler2", new ChannelInboundHandlerAdapter() {
                                    // 没有了编解码器，这里的 msg 就是 ByteBuf 类型
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buffer = (ByteBuf) msg;
                                        log.info("handler2 received:{}", buffer.toString(StandardCharsets.UTF_8));
                                    }
                                });
                            }
                        })
                .bind(new InetSocketAddress("localhost", 8888));

    }


}
