package com.hk.reactive.netty.demo.c5;

import com.google.common.base.Strings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @author : HK意境
 * @ClassName : TestByteBuf
 * @date : 2022/12/26 17:46
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class TestByteBuf2 {

    public static void main(String[] args) {

        // 创建 ByteBuf
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        // buffer.writeCharSequence(Strings.repeat("a", 32), StandardCharsets.UTF_8);
        buffer.writeInt(5);
        buffer.writeInt(6);
        log(buffer);

        // 读取
        System.out.println(buffer.readInt());
        log(buffer);
        System.out.println(buffer.readInt());
        log(buffer);
        buffer.writeInt(7);
        log(buffer);

        // byteBuf 引用计数 -1
        buffer.release();
    }

    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }


}
