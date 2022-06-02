package com.example.commontool.javaconcurrent.juc;

import java.util.concurrent.*;

/**
 * @ClassName ExecutorTest
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/30
 **/
public class ExecutorTest {
    public static void main(String[] args) {
        ExecutorService es1 = Executors.newFixedThreadPool(10);
        ExecutorService es2 = Executors.newCachedThreadPool();
        ExecutorService es3 = Executors.newSingleThreadExecutor();
        ExecutorService es4=new ThreadPoolExecutor(10,20,1, TimeUnit.SECONDS,new LinkedBlockingQueue<>(10));

        for (int i = 0; i < 100; i++) {
            final int j=i+1;
            es4.submit(()->{
                try {
                    System.out.println(Thread.currentThread().getName()+"handle "+(j+1)+"task");
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
