package com.hk.reactive.nio.buffer;

import com.hk.reactive.nio.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * @author : HK意境
 * @ClassName : TestBufferCase2
 * @date : 2022/12/19 12:50
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class TestBufferCase2 {


    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10);
        byteBuffer.put(new byte[]{'a', 'b', 'c', 'd', 'e', 'f'});

        byteBuffer.flip();

        byteBuffer.get(new byte[4]);
        ByteBufferUtil.debugAll(byteBuffer);

        // rewind 从头开始读，将 position = 0
        byteBuffer.rewind();
        System.out.println((char) byteBuffer.get());
        System.out.println((char) byteBuffer.get());


        // mark 和 reset
        // mark 做一个当前 position 的标记，reset 将 position 重置到 mark 标记的位置
        byteBuffer.mark();
        System.out.println((char) byteBuffer.get());
        System.out.println((char) byteBuffer.get());


        byteBuffer.reset();
        System.out.println((char) byteBuffer.get());
        System.out.println((char) byteBuffer.get());

        // rewind
        byteBuffer.rewind();
        // get(int index) : 获取指定position 元素，position 位置不会改变
        byteBuffer.get(3);
        ByteBufferUtil.debugAll(byteBuffer);


    }
}
