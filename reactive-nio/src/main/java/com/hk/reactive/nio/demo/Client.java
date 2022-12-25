package com.hk.reactive.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author : HK意境
 * @ClassName : Client
 * @date : 2022/12/19 15:01
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
        boolean localhost = clientSocket.connect(new InetSocketAddress("localhost", 8888));

        if (localhost) {
            System.out.println("连接成功：");
        }

        // 发送数据
        clientSocket.write(Charset.defaultCharset().encode("hello\nworld\n11112222\n3333\n4444"));
        clientSocket.write(Charset.defaultCharset().encode("helloworld111122223333\n4444"));

        System.in.read();
        /*Scanner scanner = new Scanner(System.in);
        String cmd = "";
        while (!Objects.equals(cmd, "end")) {
            cmd = scanner.nextLine();
            clientSocket.write(ByteBuffer.wrap(cmd.getBytes(StandardCharsets.UTF_8)));
        }*/


    }


}
