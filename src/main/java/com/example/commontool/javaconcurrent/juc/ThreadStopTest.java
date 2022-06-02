package com.example.commontool.javaconcurrent.juc;

/**
 * @ClassName ThreadStopTest
 * @Description 线程停止
 * @Author tianhuan
 * @Date 2022/6/1
 **/
public class ThreadStopTest {
    private static Object lock = new Object();

    static  int i=0;

    public static void main(String[] args) {
        //线程停止方法
        //1、原生线程停止方法，过于暴力，直接停止正在执行的线程，会释放锁
        //stopTest();
        // 2、中断机制，设置线程中断标识，不会停止线程
        //interruptTest();
        // 3、利用中断机制停止线程
        interruptStopThreadTest();


    }

    private static void stopTest() {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "获取锁");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "执行完");
            }
        });

        t1.start();


        Thread t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "等待获取锁");
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "执行完");
            }
        });
        t2.start();
        try {
            Thread.sleep(2000);
            t1.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void interruptTest(){
        Thread t2 = new Thread(() -> {

            while (true){
                i++;
                System.out.println(i);
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("*******线程中断*********");
                }
                if (i==100){
                    break;
                }
                if (i==50){
                    // 清除中断标识
                    Thread.interrupted();
                }
            }
        });
        t2.start();
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // 设置中断标识
        t2.interrupt();
    }
    private static void interruptStopThreadTest(){
        Thread t2 = new Thread(() -> {
            int count=0;
            while (!Thread.currentThread().isInterrupted()&&count<1000){
                count++;
                System.out.println("count = " + count);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // sleep、wait方法会清除中断标识，所以需要重新设置回标识
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("线程停止....");
        });
        t2.start();
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 设置中断标识
        t2.interrupt();
    }

}
