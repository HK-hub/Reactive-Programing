package com.hk.reactive.nio.demo.c5;

import com.hk.reactive.nio.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author : HK意境
 * @ClassName : MultiThreadServer
 * @date : 2022/12/25 18:51
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class MultiThreadServer {

    public static void main(String[] args) throws IOException {

        Thread.currentThread().setName("thread-boss");

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        Selector boss = Selector.open();
        serverSocketChannel.register(boss, SelectionKey.OP_ACCEPT);

        serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));

        // 创建固定数量的 worker, 并初始化
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("workers-" + i);
        }
        AtomicInteger index = new AtomicInteger(0);

        while (true) {
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    // 建立连接
                    SocketChannel clientSocket = serverSocketChannel.accept();
                    clientSocket.configureBlocking(false);
                    System.out.println("建立连接成功：" + clientSocket);
                    // 关联selector
                    log.info("before register: {}", clientSocket.getRemoteAddress());
                    // 轮询进行注册
                    workers[index.incrementAndGet() % workers.length].register(clientSocket);
                    log.info("after register: {}", clientSocket.getRemoteAddress());
                }
            }
        }
    }



    // worker
    private static class Worker implements Runnable{

        private Thread thread;
        private Selector selector;
        private String name;
        private volatile boolean started = false;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();

        public Worker(String name) {
            this.name = name;
        }


        // 初始化线程和selector
        public void register(SocketChannel socketChannel) throws IOException {
            if (Objects.equals(this.started, false)) {
                this.thread = new Thread(this, this.name);
                this.selector = Selector.open();
                this.started = true;
                this.thread.start();
            }

            // 向队列添加任务，但这个任务并没有立即执行
            this.queue.add(() -> {
                try {
                    socketChannel.register(this.selector, SelectionKey.OP_READ, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            // 结束 selector 的 select() 调用阻塞: 结束select调用，唤醒阻塞
            this.selector.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    this.selector.select();
                    // 获取队列内任务执行
                    Runnable registerTask = this.queue.poll();
                    if (Objects.nonNull(registerTask)) {
                        // 注册 socket 到 selector
                        registerTask.run();
                    }

                    Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        // 可读事件
                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            clientChannel.configureBlocking(false);

                            // 读取数据
                            ByteBuffer buffer = ByteBuffer.allocateDirect(16);
                            log.info("read....{}", clientChannel.getRemoteAddress());
                            int read = clientChannel.read(buffer);
                            buffer.flip();
                            ByteBufferUtil.debugAll(buffer);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
