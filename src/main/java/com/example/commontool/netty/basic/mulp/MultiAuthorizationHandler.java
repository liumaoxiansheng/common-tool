package com.example.commontool.netty.basic.mulp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @ClassName MultiAuthorizationHandler
 * @Description 授权--入站
 * @Author tianhuan
 * @Date 2022/5/13
 **/
public class MultiAuthorizationHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("授权成功.....");
        ByteBuf in=(ByteBuf) msg;
        String s = in.toString(CharsetUtil.UTF_8);
        System.out.println("Server Authorization accept: "+ s);
        ctx.fireChannelRead(msg);
    }
}
