package com.example.commontool.javaconcurrent.juc.sync;

/**
 * @ClassName SynchronizedTest
 * @Description 同步锁测试
 * @Author tianhuan
 * @Date 2022/6/6
 **/
public class SynchronizedTest {
    static int i=0;

    static Object lock=new Object();

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            for (int i1 = 0; i1 < 5000; i1++) {
                increasingAdd();
            }
        }, "thread1");
        Thread thread2 = new Thread(() -> {
            for (int i1 = 0; i1 < 5000; i1++) {
                increasingSub();
            }
        }, "thread2");

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("i="+i);
    }

    static synchronized void increasingAdd(){
        i++;
    }
    static  void increasingAdd2(){
        synchronized (lock){
            i++;
        }
    }

    static synchronized void increasingSub(){
        i--;
    }
    static  void increasingSub2(){
        synchronized (lock){
            i--;
        }
    }
}
