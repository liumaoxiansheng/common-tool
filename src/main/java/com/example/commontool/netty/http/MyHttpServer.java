package com.example.commontool.netty.http;

import com.example.commontool.netty.NettyConst;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * @ClassName MyHttpServer
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/17
 **/
public class MyHttpServer {

    /** Nio接受连接和处理连接*/
    private static EventLoopGroup group=new NioEventLoopGroup();

    /** 服务端bootstrap */
    private static ServerBootstrap bootstrap=new ServerBootstrap();

    public static final boolean SSL = false;/*是否开启SSL模式*/

    public static void main(String[] args) throws Exception{
        final SslContext sslCtx;
        if(SSL){
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(),
                    ssc.privateKey()).build();

        }else{
            sslCtx = null;
        }
        int port=NettyConst.ECHO_PORT;
        try {
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ServerHandlerInit(sslCtx));
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println("服务端启动成功,端口是:"+port);
            System.out.println("服务器启动模式： "+( SSL ? "SSL安全模式" :"普通模式"));
            future.channel().closeFuture().sync();
            System.out.println("服务端已关闭:");
        }finally {
            group.shutdownGracefully().sync();
        }
    }

}
