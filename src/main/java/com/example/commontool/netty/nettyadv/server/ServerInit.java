package com.example.commontool.netty.nettyadv.server;

import com.example.commontool.netty.http.BusinessHandler;
import com.example.commontool.netty.nettyadv.codec.KryoDecoder;
import com.example.commontool.netty.nettyadv.codec.KryoEncoder;
import com.example.commontool.netty.nettyadv.server.asyntask.BusinessProcessor;
import com.example.commontool.netty.nettyadv.server.asyntask.DefaultTaskProcessor;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @ClassName ServerInit
 * @Description 服务组件初始化
 * @Author tianhuan
 * @Date 2022/5/19
 **/
public class ServerInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        //半包/粘包处理 链路建立、序列化、心跳超时、心跳机制、重连机制、重复登录保护、
        ChannelPipeline pipeline = sc.pipeline();

        // 监控统计
      //  pipeline.addLast(new MetricsHandler());

        // 读空闲检测
        pipeline.addLast(new CheckReadIdleHandler(10));

        // 半包/粘包处理
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
        pipeline.addLast(new LengthFieldPrepender(2));

        // 序列化
        pipeline.addLast(new KryoDecoder());
        pipeline.addLast(new KryoEncoder());

        // 心跳超时,读空闲检测
        pipeline.addLast(new ReadTimeoutHandler(30));

        // 登录、心跳响应、业务处理
        pipeline.addLast(new LoginAuthRespHandler());
        pipeline.addLast(new HeartBeatRespHandler());
        pipeline.addLast(new ServerBusinessHandler(new DefaultTaskProcessor()));



    }
}
