package com.example.commontool.javaconcurrent.juc.sync;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName EscapeTest
 * @Description 逃逸分析
 * @Author tianhuan
 * @Date 2022/6/9
 **/
@Slf4j
public class EscapeTest {

    /**
     * 逃逸分析
     *关闭逃逸分析，同时调大堆空间，避免堆内GC的发生，如果有GC信息将会被打印出来
     *VM运行参数：‐Xmx4G ‐Xms4G ‐XX:‐DoEscapeAnalysis ‐XX:+PrintGCDetails ‐XX:+Heap DumpOnOutOfMemoryError
     * 开启逃逸分析 jdk8默认开启
     * VM运行参数：‐Xmx4G ‐Xms4G ‐XX:+DoEscapeAnalysis ‐XX:+PrintGCDetails ‐XX:+Heap DumpOnOutOfMemoryError
     *
     * jps 查看进程
     * jmap ‐histo 进程ID
     **/
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {
            alloc();
        }

        long end = System.currentTimeMillis();
        log.info("执行时间：" + (end - start) + " ms");
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * * JIT编译时会对代码进行逃逸分析
     * * 并不是所有对象存放在堆区，有的一部分存在线程栈空间
     * * Ponit没有逃逸
     */
    private static String alloc() {
        Point point = new Point();
        return point.toString();
    }

    /**
     * 同步省略（锁消除） JIT编译阶段优化，JIT经过逃逸分析之后发现无线程安全问题，就会做锁消除
     **/
    public void append(String val1, String val2) {
        StringBuffer sb = new StringBuffer();
        sb.append(val1).append(val2);
    }

    /**
     * 标量替换  *
     */
    private static void test2() {
        Point point = new Point(1, 2);
        System.out.println("point.x=" + point.getX() + "; point.y=" + point.getY());
        // int x=1;
        // int y=2;
        // System.out.println("point.x="+x+"; point.y="+y);
    }


}
