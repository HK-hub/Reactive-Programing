package com.hk.reactive.netty.demo.c7;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author : HK意境
 * @ClassName : TestHttp
 * @date : 2022/12/27 23:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class TestHttp {

    public static void main(String[] args) {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new HttpServerCodec());
                            // SimpleChannelInboundHandler<T> 处理器只对指定的泛型感兴趣
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                                    log.info("请求行:{}", msg.uri());
                                    log.info("请求头：{}", msg.headers());

                                    // 返回响应
                                    DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                    byte[] data = "<h1>Hello World</h1>".getBytes(StandardCharsets.UTF_8);
                                    response.content().writeBytes(data);
                                    response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, data.length);

                                    // 写回响应

                                    ctx.writeAndFlush(response);

                                }
                            });
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    log.info("连接建立成功：{}", ctx.channel());
                                    super.channelActive(ctx);
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.info("请求：{},{}", msg.getClass(), msg.toString());

                                    if (msg instanceof HttpRequest) {
                                        // 请求行，请求头
                                    } else if (msg instanceof HttpContent) {
                                        // 请求体
                                    }


                                    super.channelRead(ctx, msg);
                                }
                            });
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind("localhost", 8080).sync();
            channelFuture.channel().closeFuture().sync();


        }catch(Exception e){
            e.printStackTrace();
            log.error("server error...", e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

}
