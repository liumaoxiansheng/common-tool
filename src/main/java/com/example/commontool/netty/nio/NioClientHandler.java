package com.example.commontool.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @ClassName NioClientHandler
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/4/29
 **/
public class NioClientHandler implements Runnable {
    private String host;
    private int port;
    private volatile boolean started;
    private Selector selector;
    private SocketChannel socketChannel;

    public NioClientHandler(String ip, int port) {
        this.host = ip;
        this.port = port;

        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 连接客户端
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // 遍历处理Selector

            while (started) {
                try {
                    selector.select(1000);
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        try {
                            handleEvent(key);
                        } catch(Exception e){
                            if(key != null){
                                key.cancel();
                                if(key.channel() != null){
                                    key.channel().close();
                                }
                            }
                        }
                    }
                }catch (Exception e){
                e.printStackTrace();
                System.exit(1);
                }
            }
        //selector关闭后会自动释放里面管理的资源
        if (selector != null) {
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleEvent(SelectionKey key) throws IOException{
        // SocketChannel 处理对应的selector
       SocketChannel sc=(SocketChannel) key.channel();

       // 连接
        if (key.isConnectable()) {
            if (sc.finishConnect()) {
                socketChannel.register(selector,SelectionKey.OP_READ);
            }else {
                System.exit(1);
            }
        }

        // 读取数据
        if (key.isReadable()) {
            //创建ByteBuffer，并开辟一个1M的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //读取请求码流，返回读取到的字节数
            int readBytes = sc.read(buffer);
            //读取到字节，对字节进行编解码
            if(readBytes>0){
                //将缓冲区当前的limit设置为position,position=0，
                // 用于后续对缓冲区的读取操作
                buffer.flip();
                //根据缓冲区可读字节数创建字节数组
                byte[] bytes = new byte[buffer.remaining()];
                //将缓冲区可读字节数组复制到新建的数组中
                buffer.get(bytes);
                String result = new String(bytes,"UTF-8");
                System.out.println("Client Received Message：" + result);
            }
            //链路已经关闭，释放资源
            else if(readBytes<0){
                key.cancel();
                sc.close();
            }
        }

    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host,port))) {
            socketChannel.register(selector,SelectionKey.OP_READ);
        }else {
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }

    }

    private static NioClientHandler clientHandler;

    public void  sendMessage(String message) throws IOException{
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        socketChannel.write(buffer);
    }

}
