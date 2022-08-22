package com.example.commontool.javaconcurrent.juc.lock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName CondtionDemo
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/6/9
 **/
public class CondtionDemo {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(()->{
            lock.lock();
            try {
                System.out.println("等着红包来吃饭...");
                    condition.await();
                System.out.println("有钱了，搞饭....");
            }catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        },"eat").start();

        new Thread(()->{
            lock.lock();
            try {
                System.out.println("搬砖搞钱...");
                Thread.sleep(1000);
                    condition.signal();
                System.out.println("钱搞完了...");
            }catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        },"money").start();
    }
}
