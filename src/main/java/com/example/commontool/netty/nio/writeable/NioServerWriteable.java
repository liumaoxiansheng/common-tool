package com.example.commontool.netty.nio.writeable;

import static com.example.commontool.netty.nio.Const.DEFAULT_PORT;

/**
 * @ClassName NioServerWriteable
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/12
 **/
public class NioServerWriteable {
    private static NoiServerHandlerWriteable nioServerHandler;
    public static void main(String[] args) {
        nioServerHandler = new NoiServerHandlerWriteable(DEFAULT_PORT);
        new Thread(nioServerHandler,"Server").start();
    }
}
