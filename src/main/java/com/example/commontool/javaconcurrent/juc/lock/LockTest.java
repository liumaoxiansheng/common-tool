package com.example.commontool.javaconcurrent.juc.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName LockTest
 * @Description lock测试
 * @Author tianhuan
 * @Date 2022/6/9
 **/
public class LockTest {
    static int i = 0;
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws Exception {

        Thread thread1 = new Thread(() -> {
            for (int i1 = 0; i1 < 50000; i1++) {
                add();
            }
        }, "thread1");

        Thread thread2 = new Thread(() -> {
            for (int i1 = 0; i1 < 50000; i1++) {
                sub();
            }
        }, "thread2");
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println("i = " + i);


    }

    public static void add() {
        lock.lock();
        try {
            i++;
        } finally {
            lock.unlock();
        }
    }

    public static void sub() {
        lock.lock();
        try {
            i--;
        } finally {
            lock.unlock();
        }
    }
}
