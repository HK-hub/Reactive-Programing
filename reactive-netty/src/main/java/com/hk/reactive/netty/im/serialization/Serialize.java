package com.hk.reactive.netty.im.serialization;

import com.hk.reactive.netty.im.message.Message;

import javax.sql.rowset.serial.SerialException;

/**
 * @author : HK意境
 * @ClassName : Serialize
 * @date : 2022/12/28 0:51
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface Serialize {

    /**
     * 序列化消息
     * @param message
     * @return
     */
    public byte[] serialize(Message message) throws SerialException;

    public Message deserialize(byte[] data) throws SerialException;

}
