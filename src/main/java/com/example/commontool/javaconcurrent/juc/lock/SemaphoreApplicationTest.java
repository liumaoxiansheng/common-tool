package com.example.commontool.javaconcurrent.juc.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName SemaphoreApplicationTest
 * @Description 信号量应用测试
 * @Author tianhuan
 * @Date 2022/6/10
 **/
public class SemaphoreApplicationTest {

    static Semaphore windows=new Semaphore(5);

    static ReentrantLock ticketLock=new ReentrantLock();

    static ExecutorService executorService = Executors.newFixedThreadPool(20);

    static int tickets=100;

    static volatile boolean hasTicket=true;

    public static void main(String[] args) {
        // 模拟窗口卖票，控制窗口流量和卖票安全
        while (hasTicket){
            executorService.execute(()->{
                sellTicket();
            });
        }
        executorService.shutdown();


    }

    public static void discount(int i){
        ticketLock.lock();
        try {
            tickets=tickets-i;
            System.out.println(Thread.currentThread().getName()+"余票数"+tickets);
        } finally {
         ticketLock.unlock();
        }
    }

    public static void sellTicket(){
        try {
            windows.acquire(1);
            try {
                Thread.sleep(10);
                if (tickets>0){
                    discount(1);
                }else {
                    System.out.println("票完了.....");
                    hasTicket=false;
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            windows.release(1);
        }
    }
}
