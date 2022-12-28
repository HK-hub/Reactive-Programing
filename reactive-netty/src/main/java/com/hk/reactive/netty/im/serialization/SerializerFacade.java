package com.hk.reactive.netty.im.serialization;

import com.hk.reactive.netty.im.message.Message;
import com.hk.reactive.netty.im.protocol.SerializationEnum;
import lombok.NonNull;

import javax.sql.rowset.serial.SerialException;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : SerializerFacade
 * @date : 2022/12/28 0:57
 * @description : 门面模式
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class SerializerFacade {

    /**
     * 序列化
     * @param serializationEnum
     * @param message
     * @return
     * @throws SerialException
     */
    public static byte[] serialize(@NonNull SerializationEnum serializationEnum, Message message) throws SerialException {
        // 获取序列化器
        AbstractSerializer serializer = chooseSerializer(serializationEnum);
        // 序列化数据
        return serializer.serialize(message);
    }

    public static Message deserialize(@NonNull SerializationEnum serializationEnum, byte[] data) throws SerialException {

        // 获取序列化器
        AbstractSerializer serializer = chooseSerializer(serializationEnum);
        // 反序列化数据
        return serializer.deserialize(data);
    }


    public static AbstractSerializer chooseSerializer(SerializationEnum serializationEnum) {
        // 选择序列化策略
        AbstractSerializer serializer = switch (serializationEnum) {
            case DEFAULT ->
                    new ProtobufSerializer();
            case JDK ->
                    new JdkSerializer();
            case JSON ->
                    new JsonSerializer();
            case XML ->
                    new XmlSerializer();
            case PROTOBUF ->
                    new ProtobufSerializer();
            case HESSIAN ->
                    new HessianSerializer();
            default ->
                    new ProtobufSerializer();
        };

        if (Objects.isNull(serializer)) {
            throw new NullPointerException("the message serializer is null");
        }
        return serializer;
    }


}
