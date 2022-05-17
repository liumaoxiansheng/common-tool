package com.example.commontool.netty.basic.mulp;

import com.example.commontool.netty.NettyConst;
import com.example.commontool.netty.basic.sample.SampleServer;
import com.example.commontool.netty.basic.sample.SampleServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @ClassName MultiServer
 * @Description 多流程server
 * @Author tianhuan
 * @Date 2022/5/13
 **/
public class MultiServer {
    private final int post;

    public MultiServer(int post) {
        this.post = post;
    }

    public static void main(String[] args) throws Exception{
        int port = NettyConst.ECHO_PORT;
        MultiServer server = new MultiServer(port);
        System.out.println("服务器即将启动");
        server.start();
        System.out.println("服务器关闭");
    }

    public void start() throws Exception {
        // 线程组
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final MultiShareableHandler shareableHandler = new MultiShareableHandler();
        // 服务端启动类
        ServerBootstrap bs = new ServerBootstrap();
        bs.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)/*指定使用NIO的通信模式*/
                .localAddress(new InetSocketAddress(post)) /*指定监听端口*/
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(shareableHandler);
                        socketChannel.pipeline().addLast(new MultiUnzipHandler());
                        socketChannel.pipeline().addLast(new MultiCompressHandler());
                        socketChannel.pipeline().addLast(new MultiDecryptionHandler());
                        socketChannel.pipeline().addLast(new MultiEncryptionHandler());
                        socketChannel.pipeline().addLast(new MultiAuthorizationHandler());
                    }
                });
        // 异步绑定到服务器 , 会阻塞到完成
        System.out.println("bs.bind().sync() ..before...");
        ChannelFuture channelFuture = bs.bind().sync();

        System.out.println("bs.bind().sync() ..after...");
        /*阻塞当前线程，直到服务器的ServerChannel被关闭*/
        channelFuture.channel().closeFuture().sync();

        System.out.println("channelFuture.channel().closeFuture().sync() ..after...");

    }
}
