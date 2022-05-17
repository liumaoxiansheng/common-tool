package com.example.commontool.netty.basic.splic.demo;

import com.example.commontool.netty.NettyConst;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName EchoClientStickyHalf
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/16
 **/
public class EchoClientStickyHalf {
    private final int port;
    private final String host;

    public EchoClientStickyHalf(int port, String host) {
        this.port = port;
        this.host = host;
    }


    public static void main(String[] args) throws InterruptedException{
        new EchoClientStickyHalf(NettyConst.ECHO_PORT,"127.0.0.1").start();
    }

    public void  start() throws InterruptedException{
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new CHandler());
            ChannelFuture future = bs.connect().sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }

    }

    public static class CHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            // lineBase 粘包半包处理
          //  socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
            socketChannel.pipeline().addLast(new ClientHandler());
        }
    }

    public static class ClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
        private AtomicInteger counter = new AtomicInteger(0);
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
            System.out.println("client Accept["+msg.toString(CharsetUtil.UTF_8)
                    +"] and the counter is:"+counter.incrementAndGet());
        }

        /*** 客户端被通知channel活跃后，做事*/
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //ByteBuf msg = null;
            String request = "他强由他强，明月照大江"
                    + System.getProperty("line.separator");
            final ByteBufAllocator alloc = ctx.alloc();
            ByteBuf msg = null;
            /*我们希望服务器接受到100个这样的报文*/
            for(int i=0;i<100;i++){
                ByteBuf byteBuf = alloc.buffer();
                msg = alloc.buffer(request.length());
                msg.writeBytes(request.getBytes());
                ctx.writeAndFlush(msg);
            }
        }

        /*** 发生异常后的处理*/
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
