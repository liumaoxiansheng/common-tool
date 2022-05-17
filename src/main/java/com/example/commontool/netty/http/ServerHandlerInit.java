package com.example.commontool.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;

/**
 * @ClassName ServerHandlerInit
 * @Description 服务handler初始化
 * @Author tianhuan
 * @Date 2022/5/17
 **/
public class ServerHandlerInit extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public ServerHandlerInit(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }
    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline cp = sc.pipeline();
        if (sslCtx!=null) {
            // ssl处理
            cp.addLast(sslCtx.newHandler(sc.alloc()));
        }

        // 编码器
        cp.addLast("encoder",new HttpResponseEncoder());

        // 解码器
        cp.addLast("decoder",new HttpRequestDecoder());

        // 编解码器 等于上面两个
       // cp.addLast(new HttpServerCodec());

        // 报文处理
        cp.addLast("aggregator",new HttpObjectAggregator(10*1024*1024));
        // 附加处理 压缩
        cp.addLast("compressor",new HttpContentCompressor());
        // 业务处理
        cp.addLast(new BusinessHandler());
    }
}
