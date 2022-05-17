package com.example.commontool.netty.basic.mulp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @ClassName MultiDecryptionHandler
 * @Description 解密--入站
 * @Author tianhuan
 * @Date 2022/5/13
 **/
public class MultiDecryptionHandler extends SimpleChannelInboundHandler {
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("解密.....");
        ByteBuf in=(ByteBuf) msg;

        String s = in.toString(CharsetUtil.UTF_8);
        System.out.println("Server accept: "+ s);
        if (s.contains("yes")) {
            // 解密成功，则往后走
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(
                    "hello 密钥正确", CharsetUtil.UTF_8));
            ctx.fireChannelRead(msg);
        }else {
            // 解密失败,直接返回无需后面
            ctx.writeAndFlush(Unpooled.copiedBuffer(
                    "密钥错误", CharsetUtil.UTF_8));
        }

    }
}
