package com.example.commontool.netty.basic.splic.demo;

import com.example.commontool.netty.NettyConst;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName EchoServerStickeyHalf
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/16
 **/
public class EchoServerStickyHalf {
    private final int port;

    public EchoServerStickyHalf(int port) {
        this.port = port;
    }
    public static void main(String[] args) {
        int port = NettyConst.ECHO_PORT;
        EchoServerStickyHalf echoSvrStickyHalf = new EchoServerStickyHalf(port);
        System.out.println("服务器即将启动");
        try {
            echoSvrStickyHalf.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("服务器关闭");
    }

    public  void start() throws InterruptedException{
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap bs = new ServerBootstrap();
            bs.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelHandler());
            ChannelFuture future = bs.bind().sync();
            System.out.println("服务器启动完成....");
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }

    }

    public static class ChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            // lineBase 粘包半包处理
          //  socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            socketChannel.pipeline().addLast(new SeverHandler());
        }
    }

    public static class SeverHandler extends ChannelInboundHandlerAdapter{
        private AtomicInteger counter = new AtomicInteger(0);

        /*** 服务端读取到网络数据后的处理*/
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf)msg;
            String request = in.toString(CharsetUtil.UTF_8);
            System.out.println("Server Accept["+request
                    +"] and the counter is:"+counter.incrementAndGet());
            String resp = "Hello,"+request+". Welcome to Netty World!"
                    + System.getProperty("line.separator");
            ctx.writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));
        }

//    /*** 服务端读取完成网络数据后的处理*/
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
//    }

        /*** 发生异常后的处理*/
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
