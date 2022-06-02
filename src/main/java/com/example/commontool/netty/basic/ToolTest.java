package com.example.commontool.netty.basic;

import io.netty.util.NettyRuntime;

/**
 * @ClassName ToolTest
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/23
 **/
public class ToolTest {
    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors() * 2);
    }
}
