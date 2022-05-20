package com.example.commontool.netty.nettyadv.client;

import com.example.commontool.netty.nettyadv.codec.KryoDecoder;
import com.example.commontool.netty.nettyadv.codec.KryoEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

/**
 * @ClassName ClientInit
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/19
 **/

public class ClientInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline pipeline = sc.pipeline();

      //  pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));


        /*连接写空闲检测*/
      //  pipeline.addLast(new CheckWriteIdleHandler(10));
        // 半包/粘包处理 链路建立、序列化、登录请求、心跳机制、业务处理、
        // 半包/粘包处理
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
        pipeline.addLast(new LengthFieldPrepender(2));

        // 序列化
        pipeline.addLast(new KryoDecoder());
        pipeline.addLast(new KryoEncoder());

      //  pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        // 登录请求
        pipeline.addLast(new LoginAuthReqHandler());

        // 心跳读超时读空闲检测
        pipeline.addLast(new ReadTimeoutHandler(30));

        // 心跳
        pipeline.addLast(new HeartBeatReqHandler());

        // 业务请求
        pipeline.addLast(new ClientBusinessHandler());


    }
}
