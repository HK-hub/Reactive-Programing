package com.hk.reactive.nio.demo.c4;

import com.google.common.base.Strings;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : WriteServer
 * @date : 2022/12/25 17:29
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class WriteServer {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // 多路复用
        Selector selector = Selector.open();
        // 注册 服务socket
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));

        // 处理事件
        while (true) {

            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                // 建立连接
                if (key.isAcceptable()) {
                    // 通过 服务端socket与客户端建立连接
                    SocketChannel clientSocket = serverSocketChannel.accept();
                    clientSocket.configureBlocking(false);
                    SelectionKey clientKey = clientSocket.register(selector, SelectionKey.OP_READ);

                    // 向客户端发送大量数据
                    String str = Strings.repeat("a", 5000000);
                    ByteBuffer buffer = Charset.defaultCharset().encode(str);

                    // 返回值代表实际写入的数据量
                    int write = clientSocket.write(buffer);
                    System.out.println("写入数据：" + write);

                    // 如果还没写完，关注后续可写事件
                    if (buffer.hasRemaining()) {
                        // 关心可写事件: 不能破坏原有关注的事件
                        clientKey.interestOps(clientKey.interestOps() | SelectionKey.OP_WRITE);
                        // 把未写完的数据挂载到 socket 上
                        clientKey.attach(buffer);
                    }

                } else if(key.isWritable()) {
                    // 可写事件
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel clientSocket = (SocketChannel) key.channel();

                    // 写数据
                    int write = clientSocket.write(buffer);
                    System.out.println("再次写入数据：" + write);

                    // 数据写完了: 执行清理操作
                    if (Objects.equals(buffer.hasRemaining(), Boolean.FALSE)) {
                        // 附件清除
                        key.attach(null);
                        // 关注事件去除
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }

                }




            }


        }


    }


}
