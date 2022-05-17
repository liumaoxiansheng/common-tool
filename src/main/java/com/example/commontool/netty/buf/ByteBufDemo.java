package com.example.commontool.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.unix.PreferredDirectByteBufAllocator;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName ByteBufDemo
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/16
 **/
public class ByteBufDemo {
    public static void main(String[] args) throws Exception {
        PreferredDirectByteBufAllocator allocator = new PreferredDirectByteBufAllocator();
        ByteBuf buffer = allocator.buffer(1024);
        File file = new File("D:\\JavaStudy\\网络\\buf/a.txt");
        buffer.writeBytes(new String("hello world!!!!").getBytes(StandardCharsets.UTF_8));
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
       writer.write(buffer.toString(CharsetUtil.UTF_8));
       writer.close();
       buffer.clear();
    }
}
