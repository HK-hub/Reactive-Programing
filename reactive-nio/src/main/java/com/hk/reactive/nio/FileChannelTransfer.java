package com.hk.reactive.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author : HK意境
 * @ClassName : FileChannelTransfer
 * @date : 2022/12/19 13:42
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class FileChannelTransfer {


    public static void main(String[] args) {

        try(
                FileChannel fromChannel = new FileInputStream("aa.txt").getChannel();
                FileChannel toChannel = new FileInputStream("aa.txt").getChannel();
        ) {

            // transfer 方法用来进行文件拷贝效率更高，底层会利用操作系统进行 零拷贝优化，限制大小2GB
            long size = fromChannel.size();
            // 不断的传输
            for (long left = size; left > 0 ; ) {
                // from 传输 left 大小，从上一次结束为止开始传输
                long transfer = fromChannel.transferTo(size - left, left, toChannel);
                left -= transfer;
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
