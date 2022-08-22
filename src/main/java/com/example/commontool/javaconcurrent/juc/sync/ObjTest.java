package com.example.commontool.javaconcurrent.juc.sync;

import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;

/**
 * @ClassName ObjTest
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/6/7
 **/
public class ObjTest {
    public static void main(String[] args) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Object obj = new Object();

       // System.out.println(ClassLayout.parseInstance(obj).toPrintable());
//       int[] arr=new int[1];
//        System.out.println(ClassLayout.parseInstance(arr).toPrintable());

        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"开始执行"+"\n"+ClassLayout.parseInstance(obj).toPrintable());
            synchronized (obj){
                obj.hashCode();
//                try {
//                    obj.wait(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                obj.notify();
//                System.out.println("notify后");
//                try {
//                    obj.wait(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                System.out.println(Thread.currentThread().getName()+"获取锁中"+"\n"+ClassLayout.parseInstance(obj).toPrintable());
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"释放锁"+"\n"+ClassLayout.parseInstance(obj).toPrintable());
        },"thread1").start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        new Thread(()->{
//            System.out.println(Thread.currentThread().getName()+"开始执行"+"\n"+ClassLayout.parseInstance(obj).toPrintable());
//            synchronized (obj){
//                //obj.notify();
//                System.out.println(Thread.currentThread().getName()+"获取锁中"+"\n"+ClassLayout.parseInstance(obj).toPrintable());
//            }
//            System.out.println(Thread.currentThread().getName()+"释放锁"+"\n"+ClassLayout.parseInstance(obj).toPrintable());
//        },"thread2").start();
    }
}
