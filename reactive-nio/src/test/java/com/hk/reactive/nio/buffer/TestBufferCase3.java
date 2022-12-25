package com.hk.reactive.nio.buffer;

import com.hk.reactive.nio.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author : HK意境
 * @ClassName : TestBufferCase3
 * @date : 2022/12/19 13:07
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class TestBufferCase3 {

    public static void main(String[] args) {


        // 字符串转换为 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16);
        byteBuffer.put("I am a String".getBytes(StandardCharsets.UTF_8));
        ByteBufferUtil.debugAll(byteBuffer);


        // 2. Charset
        ByteBuffer encodeBuffer = StandardCharsets.UTF_8.encode("hello");
        ByteBufferUtil.debugAll(encodeBuffer);


        // 3. Wrap
        ByteBuffer wrapBuffer = ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8));
        ByteBufferUtil.debugAll(wrapBuffer);



    }


}
