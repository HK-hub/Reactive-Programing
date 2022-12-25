package com.hk.reactive.netty.demo.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

/**
 * @author : HK意境
 * @ClassName : HelloServer
 * @date : 2022/12/25 20:34
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class HelloServer {

    public static void main(String[] args) {
        // 1. 启动器，负责组装netty 组件，启动服务器
        new ServerBootstrap()
                // 2. BossEventLoop, WorkerEventLoop(selector, thread), group 组
                .group(new NioEventLoopGroup())
                // 3. 选择服务器的ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                // 4. boss 负责处理连接，worker(child) 负责理出数据读写，决定了worker(child) 能够执行那些操作(handler)
                .childHandler(
                    // 5. channel 代表和客户端进行数据读写的通道， Initializer 初始化，负责添加别的handler
                    new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        // 6. 添加具体的handler
                        channel.pipeline().addLast(new StringDecoder());    // 讲 ByteBuf 转换为字符串
                        //
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {     // 自定义 handler
                            @Override   // 读时间
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // 打印上一步转换好的 字符串
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // 7. 绑定监听端口
                .bind(new InetSocketAddress("localhost", 8888));


    }

}
