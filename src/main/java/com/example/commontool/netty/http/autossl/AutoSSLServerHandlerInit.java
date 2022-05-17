package com.example.commontool.netty.http.autossl;

import com.example.commontool.netty.http.BusinessHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.OptionalSslHandler;
import io.netty.handler.ssl.SslContext;

/**
 * @ClassName AutoSSLServerHandler
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/17
 **/
public class AutoSSLServerHandlerInit extends ChannelInitializer<SocketChannel> {

    private SslContext sslContext;

    public AutoSSLServerHandlerInit(SslContext sslContext){
        this.sslContext=sslContext;
    }

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline cp = sc.pipeline();
        // ssl和http请求自动处理
        cp.addLast(new OptionalSslHandler(sslContext));

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
