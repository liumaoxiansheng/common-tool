package com.example.commontool.netty.basic.serializable.msgpack;

import com.example.commontool.netty.basic.serializable.msgpack.vo.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @ClassName MsgPackDecoder
 * @Description 解码器
 * @Author tianhuan
 * @Date 2022/5/17
 **/
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> list) throws Exception {
        int bytes = msg.readableBytes();
        final byte[] array=new byte[bytes];
        msg.getBytes(msg.readerIndex(),array,0,bytes);
        MessagePack messagePack = new MessagePack();
        User read = messagePack.read(array, User.class);
        list.add(read);
    }
}
