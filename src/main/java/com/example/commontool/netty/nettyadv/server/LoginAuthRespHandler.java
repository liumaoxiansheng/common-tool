package com.example.commontool.netty.nettyadv.server;

import com.example.commontool.netty.nettyadv.vo.MessageCode;
import com.example.commontool.netty.nettyadv.vo.MessageType;
import com.example.commontool.netty.nettyadv.vo.MsgHeader;
import com.example.commontool.netty.nettyadv.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @ClassName LoginAuthRespHandler
 * @Description 登录认证
 * @Author tianhuan
 * @Date 2022/5/19
 **/
@Slf4j
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage message=(MyMessage) msg;
        MsgHeader header = message.getMyHeader();
        // 判断请求
        if (header!=null && header.getType()== MessageType.LOGIN_REQ.value()) {
            log.info("收到客户端认证请求 : {}" , message);
            MyMessage loginResp = null;
            String nodeIdx = ctx.channel().remoteAddress().toString();
            log.info("节点信息：{}",nodeIdx);
            // 重复登录判断
            if (SecurityCenter.isDupLog(nodeIdx)) {
                loginResp=buildResponse(MessageCode.DUP_LOGIN);
                log.warn("拒绝重复登录，应答消息 : {}" , loginResp);
                ctx.writeAndFlush(loginResp);
                ctx.close();
            }else {
              InetSocketAddress address=(InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                // 白名单检测
                if (SecurityCenter.isWhiteIP(ip)) {
                    SecurityCenter.addLoginUser(nodeIdx);
                    loginResp = buildResponse(MessageCode.LOGIN_SUCCESS);
                    log.info("认证通过，应答消息 : {}" , loginResp);
                    ctx.writeAndFlush(loginResp);
                }else {
                    loginResp = buildResponse(MessageCode.LOGIN_FAIL);
                    log.warn("认证失败，应答消息 : {}" , loginResp);
                    ctx.writeAndFlush(loginResp);
                    ctx.close();
                }
            }

            // 释放资源
            ReferenceCountUtil.release(msg);
        }else {

            ctx.fireChannelRead(msg);
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        SecurityCenter.removeLoginUser(ctx.channel().remoteAddress().toString());
        ctx.close();
    }

    private MyMessage buildResponse(MessageCode result) {
        MyMessage message = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setType(MessageType.LOGIN_RESP.value());
        message.setMyHeader(msgHeader);
        message.setBody(result);
        return message;
    }
}
