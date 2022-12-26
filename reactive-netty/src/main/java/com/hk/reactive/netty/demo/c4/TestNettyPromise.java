package com.hk.reactive.netty.demo.c4;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : TestNettyPromise
 * @date : 2022/12/26 16:46
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class TestNettyPromise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 1. 准备 EventLoop 对象
        EventLoop eventLoop = new NioEventLoopGroup().next();
        // 2. 可以主动创建 promise, 结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {
            // 3. 任意一个线程执行计算，计算完毕后，向promise 容器填充结果
            log.info("开始计算");
            try {
                TimeUnit.SECONDS.sleep(1);
                int i = 10 / 0;
                // 4. 填充结果
                promise.setSuccess(80);
            } catch (InterruptedException e) {
                promise.setFailure(e);
                e.printStackTrace();
            }

        }).start();

        log.info("等待结果...");
        // 5. 接收结果的线程
        log.info("获取结果:{}", promise.get());


    }

}
