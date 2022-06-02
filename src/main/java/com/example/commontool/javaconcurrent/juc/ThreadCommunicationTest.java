package com.example.commontool.javaconcurrent.juc;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName ThreadCommunicationTest
 * @Description 线程通讯测试
 * @Author tianhuan
 * @Date 2022/6/1
 **/
public class ThreadCommunicationTest {
    private static volatile boolean flag = true;

    private static boolean flag1 = true;
    private static Object lock = new Object();

    public static void main(String[] args) {

        // 1、volatile
        //volatileCommunicate();
        // 2、等待唤醒通知,wait 需要和synchronized一起使用，并且需要wait和notify有先后顺序，一般用notifyAll,notify只会随机的唤醒wait队列中的一个线程，由操作系统决定。
       // wailNotifyTest();
        // 3、LockSupport,LockSupport是JDK中用来实现线程阻塞和唤醒的工具，线程调用park则等待“许可”，调用 unpark则为指定线程提供“许可”。
        // 使用它可以在任何场合使线程阻塞，可以指定任何线程进行 唤醒，并且不用担心阻塞和唤醒操作的顺序，但要注意连续多次唤醒的效果和一次唤醒是一样的。
       // lockSupportTest();
        // 4、管道输入输出流
        pipeTest();


    }

    private static void volatileCommunicate() {
        new Thread(() -> {
            while (flag) {
                System.out.println(Thread.currentThread().getName() + "执行中");
            }
            System.out.println(Thread.currentThread().getName() + "停止");
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            flag = false;
            System.out.println(Thread.currentThread().getName() + "终止标识");
        }).start();
    }

    private static void wailNotifyTest() {
        new Thread(() -> {
            synchronized (lock) {
                while (flag1) {
                    System.out.println("wait start...");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("wait end...");
                }
            }

        }).start();

        new Thread(() -> {
            if (flag1) {
                synchronized (lock) {
                    lock.notify();
                    System.out.println("notify.....");
                    flag1 = false;
                }
            }

        }).start();
    }

    private static void lockSupportTest() {
        Thread thread = new Thread(() -> {
            while (flag1) {
                System.out.println("LockSupport.park start...");
                LockSupport.park();
                System.out.println("LockSupport.park end...");
            }
        });
        new Thread(() -> {
            if (flag1) {
                LockSupport.unpark(thread);
                System.out.println("LockSupport.unpark.....");
                flag1 = false;
            }

        }).start();
        thread.start();


    }

    private static void pipeTest(){
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        try {
            out.connect(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread printThread = new Thread(new Print(in), "PrintThread");
        printThread.start();
        int recive=0;
        try {
            while ((recive=System.in.read())!=-1){
                out.write(recive);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static class Print implements Runnable{

        private PipedReader reader;
        public Print(PipedReader in){
            this.reader=in;
        }

        @Override
        public void run() {
            int recive=0;
            try {
                while ((recive=reader.read())!=-1){
                    System.out.print("收到："+(char) recive);
                }
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
