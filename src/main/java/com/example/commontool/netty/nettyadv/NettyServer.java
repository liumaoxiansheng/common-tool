package com.example.commontool.netty.nettyadv;

import com.example.commontool.netty.NettyConst;
import com.example.commontool.netty.nettyadv.server.ServerInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @ClassName NettyServer
 * @Description 服务端入口
 * @Author tianhuan
 * @Date 2022/5/19
 **/
@Slf4j
public class NettyServer {

    public static void main(String[] args) throws Exception{
        new NettyServer().start();
    }

    public void start() throws Exception{
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("nt_boss"));
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors(), new DefaultThreadFactory("nt_worker"));
        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .localAddress(new InetSocketAddress(NettyConst.SERVER_PORT))
                    .childHandler(new ServerInit());
            b.bind().sync();
            log.info("Netty server start : {}::{}",NettyConst.SERVER_IP,NettyConst.SERVER_PORT);
        }finally {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }

    }
}
