package com.example.commontool.javaconcurrent.juc.sync;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SyncQModeDemo
 * @Description 根据QMode的不同，会执行不同的唤醒策略
 * @Author tianhuan
 * @Date 2022/6/7
 **/
@Slf4j
public class SyncQModeDemo {

    public static void main(String[] args) throws InterruptedException {

        SyncQModeDemo demo = new SyncQModeDemo();

        demo.startThreadA();
        //控制线程执行时间
        Thread.sleep(100);
        demo.startThreadB();
        Thread.sleep(100);
        demo.startThreadC();
    }

    final Object lock = new Object();

    public void startThreadA() {
        new Thread(() -> {
            synchronized (lock) {
                log.debug("A get lock");
                try {
                   // Thread.sleep(500);
                    lock.wait(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("A release lock");
                lock.notifyAll();
            }
        }, "thread-A").start();
    }

    public void startThreadB() {
        new Thread(() -> {
            synchronized (lock) {
                try {
                    log.debug("B get lock");
                    //Thread.sleep(400);
                    //lock.wait(400);
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("B release lock");
            }
        }, "thread-B").start();
    }

    public void startThreadC() {
        new Thread(() -> {
            synchronized (lock) {

                log.debug("C get lock");
                try {
                    //lock.wait(300);
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("C release lock");
            }
        }, "thread-C").start();
    }

}
