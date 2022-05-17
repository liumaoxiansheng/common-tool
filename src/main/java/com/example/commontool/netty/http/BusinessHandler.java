package com.example.commontool.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName BusinessHandler
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/17
 **/
public class BusinessHandler extends ChannelInboundHandlerAdapter {

    /**
     * 发送的返回值
     * @param ctx     返回
     * @param context 消息
     * @param status 状态
     */
    private void send(ChannelHandlerContext ctx, String context,
                      HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,status,
                Unpooled.copiedBuffer(context, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String result="";
        FullHttpRequest httpRequest = (FullHttpRequest)msg;
        System.out.println(httpRequest.headers());
        try {
            String uri = httpRequest.getUri();
            String body = httpRequest.content().toString(StandardCharsets.UTF_8);
            HttpMethod method = httpRequest.getMethod();
            System.out.println("接收到:"+method+" 请求");
            if (uri.contains("/test")) {
                result="非法请求"+uri+",请求不允许含/test";
                send(ctx,result, HttpResponseStatus.BAD_REQUEST);
                return;
            }
            if (method.equals(HttpMethod.GET)) {
                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="GET请求,应答:"+RespConstant.getNews();
                send(ctx,result,HttpResponseStatus.OK);
                return;
            }
            //如果是其他类型请求，如post
            if(HttpMethod.POST.equals(method)){
                //接受到的消息，做业务逻辑处理...
                //....
                return;
            }
        }catch (Exception e){
            System.out.println("处理请求失败!");
            e.printStackTrace();
        }

    }

    /**
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
    }
}
