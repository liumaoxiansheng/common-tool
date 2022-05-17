package com.example.commontool.netty.basic.mulp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

/**
 * @ClassName MultiCompressHandler
 * @Description 压缩handler--出站
 * @Author tianhuan
 * @Date 2022/5/13
 **/
public class MultiCompressHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
           System.out.println("压缩......");
           super.read(ctx);
    }
}
