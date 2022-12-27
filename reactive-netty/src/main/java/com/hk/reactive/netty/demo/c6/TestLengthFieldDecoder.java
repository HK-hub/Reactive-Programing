package com.hk.reactive.netty.demo.c6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author : HK意境
 * @ClassName : TestLengthFieldDecoder
 * @date : 2022/12/27 22:59
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class TestLengthFieldDecoder {

    public static void main(String[] args) {

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,
                        0,
                        4,
                        1,
                        4),
                new LoggingHandler(LogLevel.DEBUG)
        );

        /*
         * --------------------------
         * | 内容长度     |  实际内容  |
         * | 4 字节      |   xxxx   |
         * -------------------------
         */
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        // 实际内容
        send(buffer, "this is acutal message content");
        send(buffer,"hello world");

        embeddedChannel.writeInbound(buffer);
    }

    private static void send(ByteBuf buffer, String content) {
        byte[] msgBytes = content.getBytes(StandardCharsets.UTF_8);

        // 内容长度
        int contentLength = msgBytes.length;

        // 写内容长度
        buffer.writeInt(contentLength);
        // 写入版本
        buffer.writeByte(1);
        // 写入实际内容
        buffer.writeBytes(msgBytes);

    }


}
