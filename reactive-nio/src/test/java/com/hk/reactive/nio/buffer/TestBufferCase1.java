package com.hk.reactive.nio.buffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * @author : HK意境
 * @ClassName : TestBufferCase1
 * @date : 2022/12/19 11:58
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class TestBufferCase1 {

    public static void main(String[] args) {

        // FileChannel
        // 1. 输入输出流，2. RandomAccessFile
        try (FileChannel channel = new FileInputStream("F:\\JavaCode\\Reactive Programing\\reactive-nio\\src\\main\\resources\\data.txt").getChannel()) {
            // 准备缓冲区，读取数据进入
            ByteBuffer buff = ByteBuffer.allocate(10);

            // 从 channel 读取数据，向buffer中写入数据
            while (channel.read(buff) != -1) {
                // 处理buffer: 切换模式
                buff.flip();
                while (buff.hasRemaining()) {
                    byte b = buff.get();
                    System.out.println((char) b);
                }
                buff.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
