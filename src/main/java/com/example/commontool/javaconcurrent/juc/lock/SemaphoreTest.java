package com.example.commontool.javaconcurrent.juc.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @ClassName SemaphoreTest
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/6/10
 **/
public class SemaphoreTest {
    static Semaphore semaphore = new Semaphore(3);
    static ExecutorService executorService = Executors.newFixedThreadPool(10);
    public static void main(String[] args) {
        while (true){
            executorService.execute(()->{
                exec();
            });
        }

    }

    public  static void exec(){
        try {
            semaphore.acquire(1);
            try {
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName()+"执行完...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release(1);
        }

    }
}
