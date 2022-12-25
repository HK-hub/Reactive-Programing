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
 * @ClassName : ReadServer
 * @date : 2022/12/23 18:12
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ReadServer {

    public static void  split(ByteBuffer source) {

        // 切换为读模式
        source.flip();

        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                // 得到一条完整消息
                // 计算长度
                int length = i - source.position() + 1;
                // 读取元素
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                ByteBufferUtil.debugAll(target);
            }

        }

        // compact: 压缩未读的到数组头部
        source.compact();
    }


    public static void main(String[] args) throws IOException {


        // 服务器端socket套接字
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 8888));

        // 非阻塞
        serverSocketChannel.configureBlocking(false);

        // selector 多路复用器
        Selector selector = Selector.open();

        // 注册 serverSocketChannel
        SelectionKey serverSocketSelectionKey = serverSocketChannel.register(selector, 0, null);
        // 设置感兴趣的事件
        serverSocketSelectionKey.interestOps(SelectionKey.OP_ACCEPT);

        // 接收socket事件
        while (true) {

            // 阻塞
            selector.select();
            // 事件到达
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            // 处理事件
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                // 去除该事件
                keyIterator.remove();

                // 处理事件
                if (selectionKey.isAcceptable()) {
                    // 客户端连接请求
                    ServerSocketChannel serverSocket = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel clientSocket = serverSocket.accept();
                    clientSocket.configureBlocking(false);

                    // 注册复用器
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16);
                    // 附件
                    SelectionKey clientSelectionKey = clientSocket.register(selector, 0, byteBuffer);
                    clientSelectionKey.interestOps(SelectionKey.OP_READ);

                    System.out.println(clientSocket);
                }
                else if (selectionKey.isReadable()) {
                    SocketChannel clientChannel = null;
                    try{
                        // 可读事件
                        clientChannel = (SocketChannel) selectionKey.channel();
                        // 区分是否是断开连接: 正常断开连接，read() 返回 -1
                        // 获取 selectionKey 上关联附件
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        int read = clientChannel.read(buffer);
                        if (read == -1) {
                            // 断开连接
                            selectionKey.cancel();
                        } else {
                            // 数据读取拆分
                            split(buffer);
                            // 扩容
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocateDirect(buffer.capacity() * 2);
                                // 切换读写模式
                                buffer.flip();
                                // 旧数据放入
                                newBuffer.put(buffer);
                                // 设置附件
                                selectionKey.attach(newBuffer);
                            }
                            ByteBufferUtil.debugRead(buffer);
                        }
                    }catch(Exception e){
                        Objects.requireNonNull(clientChannel);
                        int port = clientChannel.socket().getPort();
                        InetAddress inetAddress = clientChannel.socket().getInetAddress();
                        System.out.println(port + inetAddress.toString() + "clientSocket 下线");
                        selectionKey.cancel();
                    }
                }

            }

        }


    }


}
