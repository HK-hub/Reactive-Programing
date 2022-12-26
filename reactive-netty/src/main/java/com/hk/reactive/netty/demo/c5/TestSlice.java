package com.hk.reactive.netty.demo.c5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @author : HK意境
 * @ClassName : TestSlice
 * @date : 2022/12/26 19:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class TestSlice {

    public static void main(String[] args) {

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        buffer.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        log(buffer);

        // 在 slice 切片过程中并没有发生内存复制
        ByteBuf slice = buffer.slice(0, 5);
        System.out.println(buffer.refCnt());
        System.out.println(slice.retain());
        // 切片 buffer 对 capacity 容量进行了限制
        // slice.writeByte(1);
        ByteBuf slice1 = buffer.slice(5, 5);
        log(slice);
        log(slice1);

        // 释放原有 buffer 内存
        buffer.release();
        System.out.println(buffer.refCnt());
        System.out.println(slice.refCnt());
        log(slice);

        slice.setByte(0, 'e');
        System.out.println("===============================================================================");
        log(slice);
        log(buffer);

        // 副本, 与原始ByteBuf 共用同一块内存
        ByteBuf duplicate = buffer.duplicate();

        // 深拷贝：进行了数据复制产生的新的 ByteBuf 对象
        ByteBuf copy = buffer.copy();


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
