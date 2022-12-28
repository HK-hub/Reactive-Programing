package com.hk.reactive.netty.im.serialization;

import com.hk.reactive.netty.im.message.Message;

import javax.sql.rowset.serial.SerialException;

/**
 * @author : HK意境
 * @ClassName : XmlSerializer
 * @date : 2022/12/28 0:54
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class XmlSerializer extends AbstractSerializer{
    @Override
    public byte[] serialize(Message message) {
        return new byte[0];
    }

    @Override
    public Message deserialize(byte[] data) throws SerialException {
        return null;
    }
}
