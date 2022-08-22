package com.example.commontool.javaconcurrent.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ConditionTest
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/6/9
 **/
@Slf4j
public class ConditionTest {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition cigCon = lock.newCondition();
    private static Condition takeCon = lock.newCondition();

    private static boolean hashcig = false;
    private static boolean hastakeout = false;

    //送烟
    public void cigratee(){
        lock.lock();
        try {
            while(!hashcig){
                try {
                    log.debug("没有烟，歇一会");
                    // 前半段： 释放锁，进入条件队列，然后阻塞线程

                    //过渡阶段： 被其他调用singal/singalAll的线程唤醒 （前提：要在同步队列中）
                    // 调用singal/singalAll的线程:  条件队列转同步队列，
                    // 可以在释放锁的时候唤醒head的后续节点所在的线程

                    // 后半段： 获取锁（ 如果有竞争，cas获取锁失败，还会阻塞），
                    // 释放锁（唤醒同步队列中head的后续节点所在的线程）
                    // 后半段的逻辑其实就是独占锁的逻辑
                    cigCon.await();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            log.debug("有烟了，干活");
        }finally {
            lock.unlock();
        }
    }

    //送外卖
    public void takeout(){
        lock.lock();
        try {
            while(!hastakeout){
                try {
                    log.debug("没有饭，歇一会");
                    takeCon.await();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            log.debug("有饭了，干活");
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ConditionTest test = new ConditionTest();
        new Thread(() ->{
            test.cigratee();
        }).start();

        new Thread(() -> {
            test.takeout();
        }).start();

        new Thread(() ->{
            lock.lock();
            try {
                hashcig = true;
                log.debug("唤醒送烟的等待线程");
                cigCon.signal();
            }finally {
                lock.unlock();
            }


        },"t1").start();

        new Thread(() ->{
            lock.lock();
            try {
                hastakeout = true;
                log.debug("唤醒送饭的等待线程");
                takeCon.signal();
            }finally {
                lock.unlock();
            }


        },"t2").start();
    }
}
