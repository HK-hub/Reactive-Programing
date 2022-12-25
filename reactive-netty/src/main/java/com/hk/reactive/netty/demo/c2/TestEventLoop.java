package com.hk.reactive.netty.demo.c2;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : TestEventLoop
 * @date : 2022/12/25 21:30
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class TestEventLoop {

    public static void main(String[] args) {

        // 1. 创建事件循环组
        EventLoopGroup group =new NioEventLoopGroup(2);  // io 事件，普通任务，定时任务

        EventLoopGroup defaultGroup = new DefaultEventLoopGroup(); // 普通事件，定时任务

        // CPU 核数 * 2
        System.out.println(NettyRuntime.availableProcessors());

        // 2. 获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        // 3. 执行普通任务
        group.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("I am a simple task");
        });

        log.debug("main");
        System.out.println("main:"+Thread.currentThread().isDaemon());


        // 4. 定时任务
        group.scheduleAtFixedRate(() -> {
            log.debug("I am a schedule task");
            System.out.println(Thread.currentThread().isDaemon());
        }, 0, 1, TimeUnit.SECONDS);

    }

}
