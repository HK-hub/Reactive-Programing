package com.hk.reactive.netty.demo.c4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author : HK意境
 * @ClassName : TestPipeline
 * @date : 2022/12/26 16:56
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class TestPipeline {

    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(3))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 1. 通过 channel 拿到 pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        // 2. 添加处理器：head -> h1 -> h2 -> h3 -> tail
                        pipeline.addLast("handler-1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("关卡1-读取消息：{}", msg);
                                // 加工消息
                                ByteBuf buf = (ByteBuf) msg;
                                String name = buf.toString(StandardCharsets.UTF_8);
                                super.channelRead(ctx, name);
                            }
                        });
                        pipeline.addLast("handler-2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("关卡2-读取消息：{}", msg);
                                // 加工为 Student 对象
                                Student student = new Student(msg.toString());
                                super.channelRead(ctx, student);
                            }
                        });
                        pipeline.addLast("handler-3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("关卡3-读取消息：{},{}", msg, msg.getClass());
                                ch.writeAndFlush("应答消息");
                                super.channelRead(ctx, msg);
                            }
                        });
                        pipeline.addLast("handler-4", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("关卡4-写消息：{}", 4);
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("handler-5", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("关卡5-写消息：{}", 5);
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("handler-6", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("关卡6-写消息：{}", 6);
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(new InetSocketAddress("localhost", 8888));


    }


    @ToString
    static class Student {

        private String name;

        public Student(String name) {
            this.name = name;
        }
    }

}
