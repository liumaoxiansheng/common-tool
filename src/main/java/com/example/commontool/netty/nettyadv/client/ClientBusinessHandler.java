package com.example.commontool.netty.nettyadv.client;

import com.example.commontool.netty.nettyadv.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ClientBusinessHandler
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/19
 **/
@Slf4j
public class ClientBusinessHandler extends SimpleChannelInboundHandler<MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyMessage msg) throws Exception {
        log.info("业务应答消息：{}",msg.toString());
    }
}
