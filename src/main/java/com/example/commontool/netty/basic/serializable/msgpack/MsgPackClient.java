package com.example.commontool.netty.basic.serializable.msgpack;

import com.example.commontool.netty.NettyConst;
import com.example.commontool.netty.basic.serializable.msgpack.vo.User;
import com.example.commontool.netty.basic.serializable.msgpack.vo.UserContact;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName MsgPackClient
 * @Description 客户端
 * @Author tianhuan
 * @Date 2022/5/17
 **/
public class MsgPackClient {

    private final String host;

    public MsgPackClient(String host){
        this.host=host;
    }

    public static void main(String[] args) throws InterruptedException{
         new MsgPackClient("127.0.0.1").start();
    }

    public void start() throws InterruptedException{
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, NettyConst.ECHO_PORT))
                    .handler(new ClientInit());
            ChannelFuture future = bs.connect().sync();
            System.out.println("服务器已连接...");
            future.channel().closeFuture().sync();
            System.out.println("结束连接...");
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static class ClientInit extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel sc) throws Exception {
            ChannelPipeline pipeline = sc.pipeline();
            // 计算一下报文的长度，然后作为报文头加在前面
            pipeline.addLast(new LengthFieldPrepender(2));
            // 对服务器的应答也要解码，解决粘包半包
            pipeline.addLast(new LineBasedFrameDecoder(1024));

            //编码序列化
            pipeline.addLast(new MsgPackEncoder());

            // 连接处理
            pipeline.addLast(new ClientHandler(5));


        }
    }

    public static class ClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

        private final int sendNumber;

        public ClientHandler(int sendNumber) {
            this.sendNumber = sendNumber;
        }

        private AtomicInteger counter = new AtomicInteger(0);

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
            System.out.println("client Accept["+msg.toString(CharsetUtil.UTF_8)
                    +"] and the counter is:"+counter.incrementAndGet());
        }

        /*** 发生异常后的处理*/
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }


        /*** 客户端被通知channel活跃后，做事*/
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            User[] users = makeUsers();
            //发送数据
            for(User user:users){
                System.out.println("Send user:"+user);
                ctx.write(user);
            }
            ctx.flush();
        }

        /*生成用户实体类的数组，以供发送*/
        private User[] makeUsers(){
            User[] users=new User[sendNumber];
            User user =null;
            for(int i=0;i<sendNumber;i++){
                user=new User();
                user.setAge(i);
                String userName = "ABCDEFG --->"+i;
                user.setUserName(userName);
                user.setId("No:"+(sendNumber-i));
                user.setUserContact(
                        new UserContact(userName+"@legend.com","133"));
                users[i]=user;
            }
            return users;
        }


    }
}
