package com.example.commontool.netty.basic.mulp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @ClassName MultiUnzipHandler
 * @Description 解压--入站
 * @Author tianhuan
 * @Date 2022/5/13
 **/
public class MultiUnzipHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("解压.....");
        ctx.fireChannelRead(msg);
    }
}
