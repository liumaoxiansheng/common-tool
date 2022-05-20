package com.example.commontool.netty.nettyadv.client;

import com.example.commontool.netty.nettyadv.vo.MessageCode;
import com.example.commontool.netty.nettyadv.vo.MessageType;
import com.example.commontool.netty.nettyadv.vo.MsgHeader;
import com.example.commontool.netty.nettyadv.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HeartBeatReqHandler
 * @Description 心跳请求
 * @Author tianhuan
 * @Date 2022/5/19
 **/
@Slf4j
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt == IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT){
            MyMessage heartBeat = buildHeatBeat();
            log.debug("写空闲，发出心跳报文维持连接：{}", heartBeat);
            ctx.writeAndFlush(heartBeat);
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("客户端心跳服务激活...");
        super.channelActive(ctx);
    }




    private MyMessage buildHeatBeat() {
        MyMessage message = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setType(MessageType.HEARTBEAT_REQ.value());
        message.setMyHeader(msgHeader);
        return message;
    }

    private MyMessage buildHeatBeatResp() {
        MyMessage message = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setType(MessageType.HEARTBEAT_RESP.value());
        message.setMyHeader(msgHeader);
        message.setBody(MessageCode.SUCCESS);
        return message;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage message = (MyMessage) msg;
        /*是不是心跳的应答*/
        if(message.getMyHeader()!=null
                &&message.getMyHeader().getType()==MessageType.HEARTBEAT_RESP.value()){
            log.debug("收到服务器心跳应答，服务器正常...");
            ReferenceCountUtil.release(msg);
        }else if(message.getMyHeader()!=null
                &&message.getMyHeader().getType()==MessageType.HEARTBEAT_REQ.value()){
            /*心跳应答报文*/
            MyMessage heartBeatResp = buildHeatBeatResp();
            log.debug("客户端心跳应答： {}",heartBeatResp);
            ctx.writeAndFlush(heartBeatResp);
            ReferenceCountUtil.release(msg);
        }
        else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(cause instanceof ReadTimeoutException){
            log.warn("服务器长时间未应答，关闭链路");
        }
        super.exceptionCaught(ctx, cause);
    }

}
