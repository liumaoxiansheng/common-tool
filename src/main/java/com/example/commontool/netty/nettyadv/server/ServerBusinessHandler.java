package com.example.commontool.netty.nettyadv.server;

import com.example.commontool.netty.nettyadv.server.asyntask.BusinessProcessor;
import com.example.commontool.netty.nettyadv.server.asyntask.ITaskProcessor;
import com.example.commontool.netty.nettyadv.vo.EncryptUtils;
import com.example.commontool.netty.nettyadv.vo.MessageType;
import com.example.commontool.netty.nettyadv.vo.MsgHeader;
import com.example.commontool.netty.nettyadv.vo.MyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ServerBusinessHandler
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/19
 **/
@Slf4j
public class ServerBusinessHandler extends SimpleChannelInboundHandler<MyMessage> {

    private ITaskProcessor taskProcessor;

    public ServerBusinessHandler(ITaskProcessor taskProcessor){
        super();
        this.taskProcessor=taskProcessor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) throws Exception {
        // 防篡改密文校验
        String headMd5 = msg.getMyHeader().getMd5();
        String calcMd5 = EncryptUtils.encryptObj(msg.getBody());
        if (!headMd5.equals(calcMd5)) {
            log.error("报文md5检查不通过：{} vs {}，关闭连接",headMd5,calcMd5);
            ctx.writeAndFlush(buildBusinessResp("报文md5检查不通过，关闭连接"));
            ctx.close();
        }
        // 请求业务处理
        log.info(msg.toString());
        if(msg.getMyHeader().getType() == MessageType.ONE_WAY.value()){
            log.debug("ONE_WAY类型消息，异步处理...");
            BusinessProcessor.submitTask(taskProcessor.execAsyncTask(msg));
        }else{
            log.debug("TWO_WAY类型消息，应答");
            // 业务处理
            ctx.writeAndFlush(buildBusinessResp("OK"));
        }
    }


    private MyMessage buildBusinessResp(String result) {
        MyMessage message = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setType(MessageType.SERVICE_RESP.value());
        message.setMyHeader(msgHeader);
        message.setBody(result);
        return message;
    }
}
