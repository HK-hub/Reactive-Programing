package com.hk.reactive.nio;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author : HK意境
 * @ClassName : ByteBufferExam
 * @date : 2022/12/19 13:21
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ByteBufferExam {

    public static void main(String[] args) {

        // 粘包，半包问题
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,World\nI'am zhangsang\nHo".getBytes(StandardCharsets.UTF_8));
        split(source);
        source.put("w are you?\n".getBytes(StandardCharsets.UTF_8));
        split(source);

    }


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

}
