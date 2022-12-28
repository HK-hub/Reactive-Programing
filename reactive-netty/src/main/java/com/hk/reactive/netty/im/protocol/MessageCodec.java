package com.hk.reactive.netty.im.protocol;

import com.hk.reactive.netty.im.message.Message;
import com.hk.reactive.netty.im.serialization.SerializerFacade;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : MessageCodec
 * @date : 2022/12/28 0:17
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {

    /**
     * 自定义协议：
     *      魔术：4字节：hkim 或者 HKIM
     *      版本：1字节：1
     *      序列化算法：1字节：protobuf
     *      指令类型: 1字节(支持127个)
     *      请求序号：4个字节
     *      正文长度：4个字节
     *      对其填充：1个字节
     *  -------------------------------16字节
     *      正文：n 字节
     */
    // 魔数
    private static final byte[] magic = "hkim".getBytes(StandardCharsets.UTF_8);
    // 版本
    private static final byte version = 1;
    // 序列化方式
    private static final byte serialization = (byte) SerializationEnum.JDK.ordinal();
    // 对其填充
    private static final byte fillByte = (byte) 0xff;


    /**
     * 编码器
     * @param channelHandlerContext
     * @param message
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        // 1. 魔数，4字节
        out.writeBytes(magic);
        // 2. 版本，1 字节
        out.writeByte(version);
        // 3. 序列化算法，1字节
        out.writeByte(serialization);
        // 4. 指令类型：1字节
        out.writeByte(message.getMessageType());
        // 5. 请求序号：4个字节
        out.writeInt(message.getSequenceId());
        // 无意义，对齐填充
        out.writeByte(fillByte);
        // 7. 获取内容的字节数组
        byte[] content = SerializerFacade.serialize(SerializationEnum.JDK, message);
        // 6. 正文长度
        out.writeInt(content.length);
        // 8. 写真实内容
        out.writeBytes(content);
    }


    /**
     * 解码器
     * @param channelHandlerContext
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        // 读取魔数：4字节
        String magicNumber = in.readBytes(4).toString(StandardCharsets.UTF_8);
        // 读取版本：1字节
        byte version = in.readByte();
        // 读取序列化算法：1字节
        byte serialization = in.readByte();
        // 读取指令类型：1字节
        byte command = in.readByte();
        // 读取序列号
        int sequenceId = in.readInt();
        // 对其填充
        in.readByte();
        // 读取内容长度
        int contentLength = in.readInt();
        // 读取内容
        byte[] bytes = new byte[contentLength];
        in.readBytes(bytes, 0, contentLength);

        // 解析Message对象
        Message message = SerializerFacade.deserialize(SerializationEnum.getInstance(serialization), bytes);

        log.debug("{},{},{},{},{},{}", magicNumber, version, serialization, command, sequenceId, contentLength);
        log.debug("message={}", message);
        // 放入输出列表，给后续handler 使用
        out.add(message);

    }



    /**
     * @ClassName : MessageCodecChecker
     * @author : HK意境
     * @date : 2022/12/28 1:19
     * @description : 编解码校验器: 校验ByteBuf 格式，内容数据
     * @Todo :
     * @Bug :
     * @Modified :
     * @Version : 1.0
     */
    private static class MessageCodecChecker {

        public static boolean checkMagicNumber() {


            return true;
        }

    }



}
