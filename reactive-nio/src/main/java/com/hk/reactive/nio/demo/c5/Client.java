package com.hk.reactive.nio.demo.c5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author : HK意境
 * @ClassName : Client
 * @date : 2022/12/25 19:16
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class Client {

    public static void main(String[] args) throws IOException {

        // 连接服务端
        SocketChannel clientSocket = SocketChannel.open();
        boolean localhost = clientSocket.connect(new InetSocketAddress("localhost", 8080));

        if (localhost) {
            System.out.println("连接成功：" + clientSocket.getRemoteAddress());
        }

        // 发送数据
        clientSocket.write(Charset.defaultCharset().encode("1234567890abcdef"));
        System.in.read();

    }

}
