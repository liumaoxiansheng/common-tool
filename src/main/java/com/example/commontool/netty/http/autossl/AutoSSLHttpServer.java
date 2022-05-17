package com.example.commontool.netty.http.autossl;

import com.example.commontool.netty.NettyConst;
import com.example.commontool.netty.http.ServerHandlerInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * @ClassName AutoSSLHttpServer
 * @Description sslhttp请求
 * @Author tianhuan
 * @Date 2022/5/17
 **/
public class AutoSSLHttpServer {
    /** Nio接受连接和处理连接*/
    private static EventLoopGroup group=new NioEventLoopGroup();

    /** 服务端bootstrap */
    private static ServerBootstrap bootstrap=new ServerBootstrap();



    public static void main(String[] args) throws Exception{
        final SslContext sslCtx;
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(),
                    ssc.privateKey()).build();
        int port= NettyConst.ECHO_PORT;
        try {
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new AutoSSLServerHandlerInit(sslCtx));
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println("服务端启动成功,端口是:"+port);
            future.channel().closeFuture().sync();
            System.out.println("服务端已关闭:");
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
