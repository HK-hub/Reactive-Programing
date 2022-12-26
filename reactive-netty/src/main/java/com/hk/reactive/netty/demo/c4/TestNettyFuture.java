package com.hk.reactive.netty.demo.c4;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : TestNettyFuture
 * @date : 2022/12/26 16:41
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class TestNettyFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // workers
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        // worker
        EventLoop eventLoop = eventLoopGroup.next();

        // 提交任务
        Future<Integer> futureTask = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                TimeUnit.SECONDS.sleep(1);
                log.info("future task");
                return 82;
            }
        });

        // 3. 获取执行结果
        /*log.info("等待结果");
        Integer result = futureTask.get();
        log.info("任务执行结果：{}", result);*/
        futureTask.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                if (future.isDone() && future.isSuccess()) {
                    log.info("异步任务执行完毕，并且成功，结果:{}", future.get());
                }
            }
        });

    }

}
