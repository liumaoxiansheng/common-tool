package com.example.commontool.javaconcurrent.juc.lock;

import cn.hutool.core.util.RandomUtil;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName CountDownLatchTest
 * @Description 并发计数阑珊测试
 * @Author tianhuan
 * @Date 2022/6/10
 **/
public class CountDownLatchTest {
    static CountDownLatch fire = new CountDownLatch(5);

    static CountDownLatch count = new CountDownLatch(1);

    public static void main(String[] args) {

        // 1、单线程阻塞
        //singleThreadBlock();

        // 多线程阻塞
        multiThreadBlock();


    }

    private static void multiThreadBlock() {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "等待执行..");
                    count.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int randomInt = RandomUtil.randomInt(10, 100);
                try {
                    Thread.sleep(randomInt);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "执行完..");

            }, "thread" + (i + 1)).start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count.countDown();
        System.out.println("资源释放....");
    }

    private static void singleThreadBlock() {
        System.out.println("开始比赛...");
        try {
            pre();
            fire.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("选手准备待续，开跑....");
    }

    public static void pre() {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                int randomInt = RandomUtil.randomInt(10, 100);
                try {
                    Thread.sleep(randomInt);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fire.countDown();
                System.out.println(Thread.currentThread().getName() + "准备好了..");

            }, "thread" + (i + 1)).start();
        }
    }
}
