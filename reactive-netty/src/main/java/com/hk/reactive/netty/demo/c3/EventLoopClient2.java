package com.hk.reactive.netty.demo.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author : HK意境
 * @ClassName : EventLoopClient
 * @date : 2022/12/25 22:03
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class EventLoopClient2 {

    public static void main(String[] args) throws InterruptedException {

        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());
                    }
                })
                // 异步非阻塞：main 线程发起调用，真正执行connect 的是 nio 线程
                .connect(new InetSocketAddress("localhost", 8888));

        // 2.1 使用 sync() 同步处理结果： 阻塞当前线程，知道 nio 线程建立连接完毕
        /*channelFuture.sync();
        // 无阻塞向下执行获取 channel
        Channel socketChannel = channelFuture.channel();
        log.info("channel: {}", socketChannel);
        socketChannel.writeAndFlush("你好 服务器0");*/

        // 2.2 使用 addListener(回调对象) 方法异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            // 在 nio 线程建立连接完毕后，会调用 operationComplete() 方法
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                // 连接建立成功的 socketChannel
                Channel clientSocketChannel = channelFuture.channel();
                log.info("连接成功：{}", clientSocketChannel);
                clientSocketChannel.writeAndFlush("我上线了：" + clientSocketChannel);
            }
        });

    }


}
