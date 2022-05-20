package com.example.commontool.netty.nettyadv;

import com.example.commontool.netty.NettyConst;
import com.example.commontool.netty.nettyadv.bo.User;
import com.example.commontool.netty.nettyadv.bo.UserContact;

import java.util.Scanner;

/**
 * @ClassName BusiClient1
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/20
 **/
public class BusiClient1 {
    public static void main(String[] args) throws Exception{
        NettyClient nettyClient = new NettyClient(NettyConst.SERVER_IP,NettyConst.SERVER_PORT);
        new Thread(nettyClient).start();
        while(!nettyClient.isConnected()){
            synchronized (nettyClient){
                nettyClient.wait();
            }
        }
        System.out.println("网络通信已准备好，可以进行业务操作了........");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.next();
            if (msg == null) {
                break;
            } else if ("quit".equals(msg.toLowerCase())) {
                nettyClient.close();
                scanner.close();
                while(nettyClient.isConnected()){
                    synchronized (nettyClient){
                        System.out.println("等待网络关闭完成....");
                        nettyClient.wait();
                    }
                }
                System.exit(1);
            } else if("th".equals(msg.toLowerCase())){
                User user = new User();
                user.setAge(19);
                String userName = "th";
                user.setUserName(userName);
                user.setId("No:1");
                user.setUserContact(
                        new UserContact(userName+"@th_legend.com",
                                "133"));
                nettyClient.send(user);
            }else if("oneway".equals(msg.toLowerCase())){
                User user = new User();
                user.setAge(23);
                String userName = "oneway";
                user.setUserName(userName);
                user.setId("No:1");
                user.setUserContact(
                        new UserContact(userName+"@th_legend.com",
                                "166"));
                nettyClient.sendOneWay(user);
            }
            else {
                nettyClient.send(msg);
            }
        }

    }
}
