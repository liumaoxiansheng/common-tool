package com.example.commontool.netty.nio;
import java.util.Scanner;

import static com.example.commontool.netty.nio.Const.DEFAULT_PORT;
import static com.example.commontool.netty.nio.Const.DEFAULT_SERVER_IP;

/**
 * @ClassName NioClient
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/5
 **/
public class NioClient {
    private static NioClientHandler nioClientHandler;
    public static void main(String[] args) throws Exception{
        start();
        Scanner scanner = new Scanner(System.in);
        while(NioClient.sendMsg(scanner.next())){};
    }

    public  static void start(){
        nioClientHandler = new NioClientHandler(DEFAULT_SERVER_IP,DEFAULT_PORT);
        new Thread(nioClientHandler,"client").start();
    }

    public static boolean sendMsg(String msg) throws Exception{
        nioClientHandler.sendMessage(msg);
        return true;
    }

}
