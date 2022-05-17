package com.example.commontool.netty.basic.sample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * @ClassName SampleClientHandler
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/11
 **/
public class SampleClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("client Accept:"+msg.toString(CharsetUtil.UTF_8));
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client channelActive .....");
        Scanner scanner = new Scanner(System.in);
        int count=10;
        while (true){
            ctx.writeAndFlush(Unpooled.copiedBuffer(
                    "Hello,"+scanner.next(), CharsetUtil.UTF_8));
            count--;
            if (count==0) {
                break;
            }
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println("Client exception .....");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client channelInactive .....");
    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client channelReadComplete .....");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("Client userEventTriggered .....");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client channelWritabilityChanged .....");
    }
}
