package com.example.commontool.netty.nio;
import static com.example.commontool.netty.nio.Const.DEFAULT_PORT;

/**
 * @ClassName NioServer
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/5
 **/
public class NioServer {
    private static NioServerHandler nioServerHandler;
    public static void main(String[] args) {
        nioServerHandler = new NioServerHandler(DEFAULT_PORT);
        new Thread(nioServerHandler,"Server").start();
    }
}
