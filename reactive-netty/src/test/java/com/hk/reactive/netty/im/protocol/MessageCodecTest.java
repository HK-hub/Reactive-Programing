package com.hk.reactive.netty.im.protocol;

import com.hk.reactive.netty.im.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;


/**
 * @author : HK意境
 * @ClassName : MessageCodecTest
 * @date : 2022/12/28 1:40
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
class MessageCodecTest {

    @Test
    void encode() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG),
                new MessageCodec());

        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123456");
        embeddedChannel.writeOutbound(message);

    }

    @Test
    void decode() throws Exception {

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                // 解决粘包，半包问题
                new LengthFieldBasedFrameDecoder(
                        4096, 12, 4, 0, 0),
                new LoggingHandler(LogLevel.DEBUG),
                new MessageCodec());

        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123456");

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message,buffer);

        // decode
        embeddedChannel.writeInbound(buffer);

    }
}