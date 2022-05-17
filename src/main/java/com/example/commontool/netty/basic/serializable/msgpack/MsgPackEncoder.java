package com.example.commontool.netty.basic.serializable.msgpack;

import com.example.commontool.netty.basic.serializable.msgpack.vo.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @ClassName MsgPackEncoder
 * @Description 编码器
 * @Author tianhuan
 * @Date 2022/5/17
 **/
public class MsgPackEncoder extends MessageToByteEncoder<User> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, User user, ByteBuf byteBuf) throws Exception {
        MessagePack messagePack = new MessagePack();
        byte[] write = messagePack.write(user);
        byteBuf.writeBytes(write);
    }
}
