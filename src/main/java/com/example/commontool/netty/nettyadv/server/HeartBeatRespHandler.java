package com.example.commontool.netty.nettyadv.server;

import com.example.commontool.netty.nettyadv.vo.MessageCode;
import com.example.commontool.netty.nettyadv.vo.MessageType;
import com.example.commontool.netty.nettyadv.vo.MsgHeader;
import com.example.commontool.netty.nettyadv.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HeartBeatRespHandler
 * @Description 心跳响应
 * @Author tianhuan
 * @Date 2022/5/19
 **/
@Slf4j
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if(evt == IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT){
            MyMessage heartBeat = buildHeatBeatReq();
            log.debug("读空闲，向客户端发出心跳报文维持连接：{}", heartBeat);
            ctx.writeAndFlush(heartBeat);
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("客户端已关闭连接====>");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage message = (MyMessage) msg;
        /*是不是心跳请求*/
        if(message.getMyHeader()!=null
                &&message.getMyHeader().getType()==MessageType.HEARTBEAT_REQ.value()){
            /*心跳应答报文*/
            MyMessage heartBeatResp = buildHeatBeat();
            log.debug("服务端心跳应答： {}",heartBeatResp);
            ctx.writeAndFlush(heartBeatResp);
            ReferenceCountUtil.release(msg);
        }else if(message.getMyHeader()!=null
                &&message.getMyHeader().getType()==MessageType.HEARTBEAT_RESP.value()){
            /*客户端心跳应答报文*/
            log.debug("收到客户端心跳应答，客户端正常...");
            ReferenceCountUtil.release(msg);
        }
        else{
            ctx.fireChannelRead(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       if (cause instanceof ReadTimeoutException){
           log.warn("客户端长时间未通信，可能已经宕机，关闭链路");
           // 移除信息
           SecurityCenter.removeLoginUser(ctx.channel().remoteAddress().toString());
           ctx.close();
       }
        super.exceptionCaught(ctx,cause);
    }

    private MyMessage buildHeatBeat() {
        MyMessage message = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setType(MessageType.HEARTBEAT_RESP.value());
        message.setMyHeader(msgHeader);
        message.setBody(MessageCode.SUCCESS);
        return message;
    }

    private MyMessage buildHeatBeatReq() {
        MyMessage message = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setType(MessageType.HEARTBEAT_REQ.value());
        message.setMyHeader(msgHeader);
        return message;
    }
}
