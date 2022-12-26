package com.hk.reactive.netty.demo.c5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @author : HK意境
 * @ClassName : TestCompositeByteBuf
 * @date : 2022/12/26 19:57
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class TestCompositeByteBuf {

    public static void main(String[] args) {

        ByteBuf buffer1 = ByteBufAllocator.DEFAULT.buffer(10);
        buffer1.writeBytes(new byte[]{1, 2, 3, 4, 5});

        ByteBuf buffer2 = ByteBufAllocator.DEFAULT.buffer(10);
        buffer2.writeBytes(new byte[]{6, 7, 8, 9, 10});

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        //buffer.writeBytes(buffer1).writeBytes(buffer2);
        log(buffer);

        System.out.println("=======================================================");

        // CompositeByteBuf
        CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        // 合并ByteBuf
        compositeByteBuf.addComponents(true, buffer1, buffer2);
        log(compositeByteBuf);


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
