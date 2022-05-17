package com.example.commontool.netty.nio.writeable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName NoiServerHandlerWriteable
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/12
 **/
public class NoiServerHandlerWriteable implements Runnable{
    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private  volatile  boolean isStart;

    public NoiServerHandlerWriteable(int port){
        try {
            // 创建Selector实例
            selector=Selector.open();
            // 创建ServerSocketChannel实例
            serverSocketChannel=ServerSocketChannel.open();

            // 设置非阻塞
            serverSocketChannel.configureBlocking(false);
            // 绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            // 注册监听事件 关注客户端连接
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Start Server ...... Listen Port is "+port);
            isStart=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isStart) {
            try {
                // 获取事件
                selector.select(1000);
                // 获取事件集合
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                 /*我们必须首先将处理过的 SelectionKey 从选定的键集合中删除。
                    如果我们没有删除处理过的键，那么它仍然会在主集合中以一个激活
                    的键出现，这会导致我们尝试再次处理它。*/
                    iterator.remove();
                    // 处理事件
                    handleEvent(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*根据输入信息拼接出一个应答信息*/
    public  String response(String msg){
        return "Hello,"+msg+",Now is "+new java.util.Date(
                System.currentTimeMillis()).toString() ;
    }

    /*处理事件*/
    private void handleEvent(SelectionKey key) throws IOException{
        if (key.isValid()) {
            // 处理客户端请求 连接 读取数据
            // 连接
            if (key.isAcceptable()) {
                // ServerSocketChannel 处理接受连接
                ServerSocketChannel ssc=(ServerSocketChannel)key.channel();
                // 接受请求
                SocketChannel sc = ssc.accept();
                System.out.println("==========The Server Has Established Connection=========");
                sc.configureBlocking(false);

                // 建立连接后，关注读取数据
                sc.register(selector,SelectionKey.OP_READ);

            }
            // 读取数据
            if (key.isReadable()) {
                // SocketChannel 与应用程序处理读写数据
                SocketChannel sc=(SocketChannel) key.channel();

                // Buffer 开辟缓存空间
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 通道读取数据
                int read = sc.read(buffer);
                if (read>0) {
                    // 只读取写入位置的数据
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String msg = new String(bytes, "UTF-8");
                    // 处理消息
                    System.out.println("Server Received Message is: "+msg);

                    // 应答客户端
                    writeToClient(sc,response(msg));

                }else if(read<0) {
                    // 小于0 取消特定的注册关系
                    key.channel();
                    sc.close();
                }
            }
            if (key.isWritable()) {
                // SocketChannel 与应用程序处理读写数据
                SocketChannel sc=(SocketChannel) key.channel();
                ByteBuffer buffer=(ByteBuffer)key.attachment();
                if (buffer.hasRemaining()) {
                    int count = sc.write(buffer);
                    System.out.println(" Server write :"+count
                            +"byte, remaining:"+buffer.hasRemaining());
                }else {
                    // 取消写注册
                    key.interestOps(SelectionKey.OP_READ);
                    // 释放buffer
                    key.attach(null);
                }
            }
        }
    }

    private void writeToClient(SocketChannel sc, String message) throws IOException{
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        //sc.write(buffer);
        sc.register(selector,SelectionKey.OP_WRITE|SelectionKey.OP_READ,buffer);
    }
}
