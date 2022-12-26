package com.hk.reactive.netty.demo.c4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author : HK意境
 * @ClassName : TestJdkFuture
 * @date : 2022/12/26 14:41
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class TestJdkFuture {


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 1. 創建線程池
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 2. 提交任务
        Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                TimeUnit.SECONDS.sleep(1);
                log.info("future task");
                return 40;
            }
        });

        // 3. 获取执行结果
        Integer result = future.get();
        log.info("任务执行结果：{}", result);


    }

}
