package com.hk.reactive.netty.im.protocol;

import com.hk.reactive.netty.im.serialization.Serialize;
import lombok.Data;
import lombok.Getter;

/**
 * @author : HK意境
 * @ClassName : SerializationEnum
 * @date : 2022/12/28 0:27
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Getter
public enum SerializationEnum {

    // 默认序列化方式：protobuf
    DEFAULT("default"),
    // jdk 序列化
    JDK("jdk"),
    // JSON 序列化
    JSON("json"),
    // Protobuf 序列化
    PROTOBUF("protobuf"),
    // XML 序列化
    XML("xml"),
    // HESSIAN 序列化
    HESSIAN("hessian"),

    ;
    private String algorithm;

    SerializationEnum(String algorithm) {
        this.algorithm = algorithm;
    }


    /**
     * 根据枚举值获取序列化方式
     * @param value
     * @return
     */
    public static SerializationEnum getInstance(int value) {

        for (SerializationEnum anEnum : SerializationEnum.values()) {
            if (anEnum.ordinal() == value) {
                return anEnum;
            }
        }
        return SerializationEnum.DEFAULT;
    }

}
