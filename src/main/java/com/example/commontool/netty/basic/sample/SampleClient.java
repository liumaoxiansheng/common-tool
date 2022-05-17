package com.example.commontool.netty.basic.sample;

import com.example.commontool.netty.NettyConst;
import com.example.commontool.netty.nio.NioClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @ClassName SampleClient
 * @Description 简单基础客户端编写
 * @Author tianhuan
 * @Date 2022/5/11
 **/
public class SampleClient {

    private final int port;
    private final String host;

    public SampleClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public static void main(String[] args)throws Exception {

        new SampleClient(NettyConst.ECHO_PORT,"127.0.0.1").start();

    }

    public  void  start() throws Exception{
        // 线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bs = new Bootstrap();
        bs.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host,port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new SampleClientHandler());
                    }
                });
        // 异步连接服务器 一直阻塞到连接上
        System.out.println("bs.connect().sync() ..before...");
        ChannelFuture future = bs.connect().sync();
        System.out.println("bs.connect().sync() ..After...");
        future.channel().closeFuture().sync();
        System.out.println("future.channel().closeFuture().sync() ..After...");
    }
}
