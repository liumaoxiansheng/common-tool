package com.example.commontool.javaconcurrent.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @ClassName ThreadCreateTest
 * @Description 线程创建方式
 * @Author tianhuan
 * @Date 2022/6/1
 **/
public class ThreadCreateTest {
    public static void main(String[] args) {
        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        // 1、Thread创建
        //  threadCreate();
        // 2、Runnable创建
        //  runnableCreate();
          //3、 Callable创建
        //callableCreate();
        // 4、线程池创建
       // threadPoolCreate();
        // 总结：本质上是Thread 的创建，调用Thread start()启动。
    }

    public static void threadCreate(){
        Thread thread = new Thread(()->{
            System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
            System.out.println("Thread create().......");
        });
        thread.start();
    }

    public static void runnableCreate(){
        Thread thread = new Thread(new MyRunnable());
        thread.start();
    }

    public static void callableCreate(){
        FutureTask<Integer> task = new FutureTask<>(new MyCallable());
        new Thread(task).start();
    }

    public static void threadPoolCreate(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(()->{
            System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
            System.out.println("ThreadPool create().......");
        });
        executorService.shutdown();
    }



    public static class MyRunnable implements Runnable{

        @Override
        public void run() {
            System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
            System.out.println("Runnable create().......");
        }
    }

    public static class  MyCallable implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
            System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
            System.out.println("Callable create().......");
            return 1;
        }
    }
}
