package com.example.commontool.netty.basic.serializable.msgpack;

import com.example.commontool.netty.NettyConst;
import com.example.commontool.netty.basic.serializable.msgpack.vo.User;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import javax.lang.model.element.VariableElement;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName MsgPackServer
 * @Description 服务器
 * @Author tianhuan
 * @Date 2022/5/17
 **/
public class MsgPackServer {

    public void start() throws InterruptedException{
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerHandler serverHandler = new ServerHandler();
        try {
            ServerBootstrap bs = new ServerBootstrap();
            bs.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(NettyConst.ECHO_PORT))
                    .childHandler(new ServerInit(serverHandler));
            ChannelFuture future = bs.bind().sync();
            System.out.println("服务器启动完成，等待客户端的连接和数据.....");
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws Exception {
        new MsgPackServer().start();
    }

    public static class ServerInit extends ChannelInitializer<SocketChannel>{

        private ServerHandler serverHandler;

        public ServerInit(ServerHandler serverHandler){
            this.serverHandler=serverHandler;
        }

        @Override
        protected void initChannel(SocketChannel sc) throws Exception {
            ChannelPipeline pipeline = sc.pipeline();
            // 粘包半包处理
            pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
            // 解码器
            pipeline.addLast(new MsgPackDecoder());
            // 业务处理
            pipeline.addLast(serverHandler);

        }
    }

    @ChannelHandler.Sharable
    public  static class ServerHandler extends ChannelInboundHandlerAdapter{
        private AtomicInteger counter = new AtomicInteger(0);

        /*** 服务端读取到网络数据后的处理*/
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //将上一个handler生成的数据强制转型
            User user = (User)msg;
            System.out.println("Server Accept["+user
                    +"] and the counter is:"+counter.incrementAndGet());
            //服务器的应答
            String resp = "I process user :"+user.getUserName()
                    + System.getProperty("line.separator");
            ctx.writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));
            ctx.fireChannelRead(user);
        }

        /*** 发生异常后的处理*/
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
