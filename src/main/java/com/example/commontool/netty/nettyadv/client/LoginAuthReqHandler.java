package com.example.commontool.netty.nettyadv.client;

import com.example.commontool.netty.nettyadv.vo.MessageCode;
import com.example.commontool.netty.nettyadv.vo.MessageType;
import com.example.commontool.netty.nettyadv.vo.MsgHeader;
import com.example.commontool.netty.nettyadv.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName LoginAuthReqHandler
 * @Description 登录请求
 * @Author tianhuan
 * @Date 2022/5/19
 **/
@Slf4j
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /*发出认证请求*/
        MyMessage loginMsg = buildLoginReq();
        log.info("请求服务器认证 : {}", loginMsg);
        ctx.writeAndFlush(loginMsg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("请求通道关闭...");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage message = (MyMessage) msg;
        /*是不是握手成功的应答*/
        if (message.getMyHeader() != null
                && message.getMyHeader().getType() == MessageType.LOGIN_RESP.value()) {
            log.info("收到认证应答报文，服务器是否验证通过？");
            MessageCode loginResult = (MessageCode) message.getBody();
            if (!MessageCode.LOGIN_SUCCESS.equals(loginResult)) {
                /*握手失败，关闭连接*/
                log.warn("未通过认证，关闭连接: {}==Error info =>{}", message, loginResult.message());
                ctx.close();
            } else {
                log.info("通过认证，移除本处理器，进入业务通信 : " + message);
                ctx.pipeline().remove(this);
                ReferenceCountUtil.release(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }


    private MyMessage buildLoginReq() {
        MyMessage message = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setType(MessageType.LOGIN_REQ.value());
        message.setMyHeader(msgHeader);
        return message;
    }
}
