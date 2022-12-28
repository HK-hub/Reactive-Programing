package com.hk.reactive.netty.im.serialization;

import com.hk.reactive.netty.im.message.Message;

import javax.sql.rowset.serial.SerialException;
import java.io.*;

/**
 * @author : HK意境
 * @ClassName : JdkSerializer
 * @date : 2022/12/28 0:53
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class JdkSerializer extends AbstractSerializer{
    @Override
    public byte[] serialize(Message message) throws SerialException {

        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            // 写入对象
            oos.writeObject(message);
            byte[] bytes = bos.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerialException("serialize the message object failed: " + e.getMessage());
        }
    }

    @Override
    public Message deserialize(byte[] data) throws SerialException {
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bis)) {

            // 写入对象
            Message message = (Message) ois.readObject();
            return message;
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerialException("serialize the message object failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SerialException("deserialize the bytes data to the message object failed: " + e.getMessage());
        }
    }


}
