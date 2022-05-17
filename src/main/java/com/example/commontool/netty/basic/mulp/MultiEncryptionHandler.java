package com.example.commontool.netty.basic.mulp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

/**
 * @ClassName MultiEncryptionHandler
 * @Description 加密handler--出站
 * @Author tianhuan
 * @Date 2022/5/13
 **/
public class MultiEncryptionHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("加密......");
        super.read(ctx);
    }
}
