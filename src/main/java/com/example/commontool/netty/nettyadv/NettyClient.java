package com.example.commontool.netty.nettyadv;

import com.example.commontool.netty.NettyConst;
import com.example.commontool.netty.nettyadv.client.ClientInit;
import com.example.commontool.netty.nettyadv.vo.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NettyClient
 * @Description 客户端
 * @Author tianhuan
 * @Date 2022/5/19
 **/
@Slf4j
public class NettyClient implements Runnable{

    /**负责重连的线程池*/
    private ScheduledExecutorService executor = Executors
            .newScheduledThreadPool(1);

    private EventLoopGroup group=new NioEventLoopGroup();

    //private Bootstrap b = new Bootstrap();

    private Channel channel;

    private String remoteAddress;
    private int port;

    public NettyClient(){}

    public NettyClient(String remoteAddress,int port) {
        this.remoteAddress=remoteAddress;
        this.port=port;
    }

    /**是否用户主动关闭连接的标志值*/
    private volatile boolean userClose = false;
    /**连接是否成功关闭的标志值*/
    private volatile boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void run() {
        try {
            connect(this.port,this.remoteAddress);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        userClose = true;
        channel.close();
    }

    /**------------测试NettyClient--------------------------*/
    public static void main(String[] args) throws Exception {
        NettyClient nettyClient = new NettyClient();
        nettyClient.connect( NettyConst.SERVER_PORT,NettyConst.SERVER_IP);
        nettyClient.send("v");
        nettyClient.close();
    }

    /**------------以下方法供业务方使用--------------------------*/
    public void send(Object message) {
        if(channel==null||!channel.isActive()){
            throw new IllegalStateException("和服务器还未未建立起有效连接！" +
                    "请稍后再试！！");
        }
        MyMessage msg = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgID(MakeMsgID.getID());
        msgHeader.setType(MessageType.SERVICE_REQ.value());
        msgHeader.setMd5(EncryptUtils.encryptObj(message));
        msg.setMyHeader(msgHeader);
        msg.setBody(message);
        channel.writeAndFlush(msg);
    }

    public void sendOneWay(Object message) {
        if(channel==null||!channel.isActive()){
            throw new IllegalStateException("和服务器还未未建立起有效连接！" +
                    "请稍后再试！！");
        }
        MyMessage msg = new MyMessage();
        MsgHeader msgHeader = new MsgHeader();
        msgHeader.setMsgID(MakeMsgID.getID());
        msgHeader.setType(MessageType.ONE_WAY.value());
        msgHeader.setMd5(EncryptUtils.encryptObj(message));
        msg.setMyHeader(msgHeader);
        msg.setBody(message);
        channel.writeAndFlush(msg);
    }



    /**连接服务器*/
    public void connect(int port,String ip)throws InterruptedException{
        try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                       // .remoteAddress(new InetSocketAddress(ip,port))
                        .handler(new ClientInit());
                ChannelFuture future = b.connect(new InetSocketAddress(ip,port)).sync();
                log.info("已连接服务器...");
                channel = future.channel();
                synchronized (this){
                    this.connected = true;
                    this.notifyAll();
                }
                channel.closeFuture().sync();
        }finally {
                if (!userClose){
                    log.warn("尝试重连服务中...");
                    executor.execute(()->{
                        try {
                            /*给操作系统足够的时间，去释放相关的资源*/
                            TimeUnit.SECONDS.sleep(1);
                            connect(port, ip);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }else {
                    // 正常关闭
                    channel=null;
                    group.shutdownGracefully().sync();
                    synchronized (this){
                        this.connected = false;
                        this.notifyAll();
                      //  executor.shutdown();
                    }
                }

        }


    }
}
