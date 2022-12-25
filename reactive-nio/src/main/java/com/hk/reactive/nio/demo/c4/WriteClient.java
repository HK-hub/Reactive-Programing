package com.hk.reactive.nio.demo.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author : HK意境
 * @ClassName : WriteClient
 * @date : 2022/12/25 18:00
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class WriteClient {

    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        //socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 8888));

        // 接收数据
        int received = 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
            int read = socketChannel.read(buffer);
            received += read;
            buffer.clear();
            System.out.println(received);
        }


    }


}
