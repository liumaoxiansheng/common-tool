package com.example.commontool.netty.http.client;

import com.example.commontool.netty.NettyConst;
import com.example.commontool.netty.http.MyHttpServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;

/**
 * @ClassName MyHttpClient
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/17
 **/
public class MyHttpClient {

    public static final String HOST  = "127.0.0.1";
    public void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpClientCodec());
                    /*聚合http为一个完整的报文*/
                    ch.pipeline().addLast("aggregator",
                            new HttpObjectAggregator(10*1024*1024));
                    /*解压缩*/
                    ch.pipeline().addLast("decompressor",
                            new HttpContentDecompressor());
                    ch.pipeline().addLast(new HttpClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        if(MyHttpServer.SSL) {
            System.out.println("服务器处于SSL模式，本客户端不支持，退出");
            return;
        }
        MyHttpClient client = new MyHttpClient();
        client.connect("127.0.0.1", NettyConst.ECHO_PORT);
    }
}
