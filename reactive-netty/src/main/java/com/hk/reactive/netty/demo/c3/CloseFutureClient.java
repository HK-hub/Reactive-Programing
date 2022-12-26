package com.hk.reactive.netty.demo.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author : HK意境
 * @ClassName : CloseFutureClient
 * @date : 2022/12/26 14:06
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class CloseFutureClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8888));

        Channel clientSocketChannel = channelFuture.sync().channel();

        // 发送消息
        new Thread("thread-send-msg") {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String line = scanner.nextLine();
                    // 退出消息
                    if (Objects.equals("q", line)) {
                        clientSocketChannel.close();
                        break;
                    }

                    // 发送消息
                    clientSocketChannel.writeAndFlush(line);
                }
            }
        }.start();

        // 获取 ClosedFuture 对象: 1.同步方式处理关闭。2.异步方式处理关闭
        ChannelFuture closeFuture = clientSocketChannel.closeFuture();
        /*log.info("waiting close...");
        closeFuture.sync();
        log.info("处理关闭后的善后工作");*/

        // 2. 异步方式
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.info("处理关闭后的善后工作");
                // 优雅停止：类比线程池的停止，不在接收新任务，把剩余任务执行完毕后停止
                worker.shutdownGracefully();
            }

        });

    }


}
