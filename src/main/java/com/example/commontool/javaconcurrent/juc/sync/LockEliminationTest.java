package com.example.commontool.javaconcurrent.juc.sync;

/**
 * @ClassName LockEliminationTest
 * @Description 线程逃逸/锁消除
 * @Author tianhuan
 * @Date 2022/6/8
 **/
public class LockEliminationTest {

    /**
     * 锁消除
     *‐XX:+EliminateLocks 开启锁消除(jdk8默认开启）
     * ‐XX:‐EliminateLocks 关闭锁消除
     * @param val1:
     * @param val2:
     * @return void
     * @Author tianhuan
     * @Date 2022/6/8
     **/
    public void append(String val1,String val2){
        StringBuffer sb = new StringBuffer();
        sb.append(val1).append(val2);
    }
    public static void main(String[] args) {
        //1、锁消除
        append1();
        //2、锁未消除
        append2();
    }

    private static void append1() {
        LockEliminationTest test = new LockEliminationTest();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            test.append("aa","bb");
        }
        long end = System.currentTimeMillis();
        System.out.println("执行时间：" + (end-start) + " ms");
    }

    public static void append2(){
        StringBuffer buffer = new StringBuffer();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            buffer.append("aa").append("bb");
        }
        long end = System.currentTimeMillis();
        System.out.println("执行时间：" + (end-start) + " ms");
    }
}
