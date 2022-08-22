package com.example.commontool.javaconcurrent.juc.lock;

import cn.hutool.core.util.RandomUtil;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName ReadWriteLockTest
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/6/13
 **/
public class ReadWriteLockTest {

    static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    static volatile  int  a=0;

    public static void main(String[] args) {

        //test1();
       //relockTest(1);


    }

    public static int relockTest(int times){
        times++;
        if (times>65535) {
            return 0;
        }
        readWriteLock.writeLock().lock();
        try {
            Thread.sleep(1);
            //a=a+RandomUtil.randomInt(5);
            System.out.println(LocalDateTime.now()+Thread.currentThread().getName()+"write==>"+times);
            return relockTest(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            readWriteLock.writeLock().unlock();
        }
        return times;
    }

    private static void test1() {
        while (true){
            for (int i = 0; i < 5; i++) {
                new Thread(()->{
                    readWriteLock.readLock().lock();
                    try {
                        Thread.sleep(RandomUtil.randomLong(2000L));
                        System.out.println(LocalDateTime.now()+Thread.currentThread().getName()+"Read==>"+a);
                    }catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        readWriteLock.readLock().unlock();
                    }
                },("ReadThread"+i)).start();
            }
            for (int i = 0; i < 5; i++) {
                new Thread(()->{
                    readWriteLock.writeLock().lock();
                    try {
                        Thread.sleep(1000);
                        a=a+RandomUtil.randomInt(5);
                        System.out.println(LocalDateTime.now()+Thread.currentThread().getName()+"write==>"+a);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        readWriteLock.writeLock().unlock();
                    }
                },("WriteThread"+i)).start();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    class CachedData {
        Object data;
        volatile boolean cacheValid;
        final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

        void processCachedData() {
            rwl.readLock().lock();
            if (!cacheValid) {
                // Must release read lock before acquiring write lock
                rwl.readLock().unlock();
                rwl.writeLock().lock();
                try {
                    // Recheck state because another thread might have
                    // acquired write lock and changed state before we did.
                    if (!cacheValid) {
                        data = new Object();
                        cacheValid = true;
                    }
                    // Downgrade by acquiring read lock before releasing write lock
                    rwl.readLock().lock();
                } finally {
                    rwl.writeLock().unlock(); // Unlock write, still hold read
                }
            }

            try {
                use(data);
            } finally {
                rwl.readLock().unlock();
            }
        }

        void use(Object o){
            System.out.println("o = " + o);
        }
    }


}
