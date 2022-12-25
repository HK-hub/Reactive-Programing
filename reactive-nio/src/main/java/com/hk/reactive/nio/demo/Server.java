package com.hk.reactive.nio.demo;

import com.hk.reactive.nio.ByteBufferUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : Server
 * @date : 2022/12/19 14:36
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class Server {

    private static boolean startServer = Boolean.TRUE;


    public static void main(String[] args) throws IOException {

        // 建立 ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 非阻塞
        serverSocketChannel.configureBlocking(Boolean.FALSE);
        // 创建 Selector 多路复用器
        Selector selector = Selector.open();

        // 注册 socket 通道: selector, 感兴趣的事件
        SelectionKey sccKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);
        System.out.println("serverSocketChannel key:" + sccKey);
        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));

        // 处理事件
        while (startServer) {

            // select() 方法，没有事件发生，阻塞线程，有事件才会恢复线程
            selector.select();
            // 处理事件: 所有发生的事件
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();

            // 处理事件
            while (selectionKeyIterator.hasNext()) {
                SelectionKey next = selectionKeyIterator.next();
                // 处理key 的时候要从事件队列里面删除
                selectionKeyIterator.remove();
                System.out.println("selected key:" + next);

                // 获取发生事件的Socket
                SelectableChannel channel = next.channel();

                // 区分事件类型
                // 服务端Socket订阅的ACCEPT 事件
                if (next.isAcceptable()) {
                    // 客户端 Socket
                    SocketChannel clientSocket = ((ServerSocketChannel) channel).accept();
                    System.out.println(clientSocket);
                    // 非阻塞
                    clientSocket.configureBlocking(false);
                    // 注册到 selector 中
                    SelectionKey selectionKey = clientSocket.register(selector, 0, null);
                    selectionKey.interestOps(SelectionKey.OP_READ);

                } else if (next.isReadable()) {
                    SocketChannel clientSocket = null;
                    try{
                        // 拿到触发事件channel
                        clientSocket = (SocketChannel) next.channel();
                        // 接收数据
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        // 客户端和服务端断开会产生一个read 事件：正常断开-1
                        int read = clientSocket.read(byteBuffer);
                        if (read == -1) {
                            // 断开连接
                            next.cancel();
                        } else {
                            // 读取数据
                            byteBuffer.flip();
                            ByteBufferUtil.debugRead(byteBuffer);
                        }

                        // 响应事件
                    }catch(Exception e){
                        Objects.requireNonNull(clientSocket);
                        int port = clientSocket.socket().getPort();
                        InetAddress inetAddress = clientSocket.socket().getInetAddress();
                        System.out.println(port + inetAddress.toString() + "clientSocket 下线");
                        next.cancel();

                    }
                } else if (next.isWritable()) {

                } else {
                    next.cancel();
                }

            }
        }
    }


}
