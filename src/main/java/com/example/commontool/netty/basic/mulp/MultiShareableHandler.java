package com.example.commontool.netty.basic.mulp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName MultiShareableHandler
 * @Description  共享handler
 * @Author tianhuan
 * @Date 2022/5/13
 **/
@ChannelHandler.Sharable
public class MultiShareableHandler extends ChannelDuplexHandler {

    public static AtomicInteger count=new AtomicInteger();
    public static AtomicInteger size=new AtomicInteger();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("流量统计结果.....");
        ByteBuf in=(ByteBuf) msg;
        System.out.println("count==>"+count.addAndGet(1));
        System.out.println("size==>"+size.addAndGet(in.toString(CharsetUtil.UTF_8).length()));
        ctx.fireChannelRead(msg);
    }
}
