package com.example.commontool.javaconcurrent.juc.sync;

import org.openjdk.jol.info.ClassLayout;

/**
 * @ClassName BasicLockTest
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/6/7
 **/
public class BasicLockTest {
    public static void main(String[] args) {
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("aa").append("bb").append("cc");
        System.out.println(ClassLayout.parseInstance(buffer).toPrintable());
    }
}
