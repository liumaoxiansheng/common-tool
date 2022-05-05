package com.example.commontool.netty.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName ServerPool
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/4/29
 **/
public class ServerPool {
    private static ExecutorService pool= Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket();
        // 绑定端口
        serverSocket.bind(new InetSocketAddress(9009));
        System.out.println("Start Server ....");
        try {
            while (true){
                // 阻塞处理连接
                pool.execute(new ServerTask(serverSocket.accept()));
            }
        }finally {
            serverSocket.close();
        }

    }

    private static class ServerTask implements Runnable{

        private Socket socket;

        public ServerTask(Socket socket){
            this.socket=socket;
        }

        @Override
        public void run() {
            //实例化与客户端通信的输入输出流
            try(ObjectInputStream inputStream =
                        new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream =
                        new ObjectOutputStream(socket.getOutputStream())){

                //接收客户端的输出，也就是服务器的输入
                String userName = inputStream.readUTF();
                System.out.println("Accept client message:"+userName);

                //服务器的输出，也就是客户端的输入
                outputStream.writeUTF("Hello,"+userName);
                outputStream.flush();
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
