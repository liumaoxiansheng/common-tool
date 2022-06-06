package com.example.commontool.javaconcurrent.juc.cas;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName ABATest
 * @Description cas ABA问题测试
 * @Author tianhuan
 * @Date 2022/6/6
 **/
public class ABATest {
    public static void main(String[] args) {

       // aba问题
       // aba();
        //解决方案 1、AtomicStampedReference处理，将数据修改版本号作为比较判断，期望值和版本对应则修改
       // abaHandle();

        // 解决方案2、AtomicMarkableReference，简化版的版本，只标记数据是否修改，不关心修改多少次
      //  abaMarkHandle();
    }

    private static void abaMarkHandle() {
        AtomicMarkableReference<Integer> atomicStamped = new AtomicMarkableReference<Integer>( 1, false);
        new Thread(() -> {
            int value = atomicStamped.getReference();
            boolean isMark = atomicStamped.isMarked();
            System.out.println("Thread1 read value: " + value+"isMark:"+isMark);
            LockSupport.parkNanos(1000000000L);
            // Thread1通过CAS修改value值为3
            if (atomicStamped.compareAndSet(value, 3,isMark,true)) {
                System.out.println("Thread1 update from " + value + " to 3");
            } else {
                System.out.println("Thread1 update fail!");
                System.out.println("Thread1 read value: " + atomicStamped.getReference()+"isMark:"+atomicStamped.isMarked());
            }
        }, "Thread1").start();

        new Thread(() -> {
            int value = atomicStamped.getReference();
            boolean isMark = atomicStamped.isMarked();
            System.out.println("Thread2 read value: " + value+"isMark:"+isMark);
            // Thread2通过CAS修改value值为2
            if (atomicStamped.compareAndSet(value, 2,isMark,true)) {
                System.out.println("Thread2 update from " + value + " to 2");
                value = atomicStamped.getReference();
                isMark = atomicStamped.isMarked();
                System.out.println("Thread2 read value: " + value+"isMark:"+isMark);
                // Thread2通过CAS再修改value值为1
                if (atomicStamped.compareAndSet(value, 1,isMark,true)) {
                    System.out.println("Thread2 update from " + value + " to 1");
                }
            } else {
                System.out.println("Thread2 update fail!");
            }
        }, "Thread2").start();
    }

    private static void abaHandle() {
        AtomicStampedReference<Integer> atomicStamped = new AtomicStampedReference<Integer>( 1, 1);
        new Thread(() -> {
            int value = atomicStamped.getReference();
            int stamp = atomicStamped.getStamp();
            System.out.println("Thread1 read value: " + value+"Stamp:"+stamp);
            LockSupport.parkNanos(1000000000L);
            // Thread1通过CAS修改value值为3
            if (atomicStamped.compareAndSet(value, 3,stamp,stamp+1)) {
                System.out.println("Thread1 update from " + value + " to 3");
            } else {
                System.out.println("Thread1 update fail!");
                System.out.println("Thread1 read value: " + atomicStamped.getReference()+"Stamp:"+atomicStamped.getStamp());
            }
        }, "Thread1").start();

        new Thread(() -> {
            int value = atomicStamped.getReference();
            int stamp = atomicStamped.getStamp();
            System.out.println("Thread2 read value: " + value+"Stamp:"+stamp);
            // Thread2通过CAS修改value值为2
            if (atomicStamped.compareAndSet(value, 2,stamp,stamp+1)) {
                System.out.println("Thread2 update from " + value + " to 2");
                 value = atomicStamped.getReference();
                 stamp = atomicStamped.getStamp();
                System.out.println("Thread2 read value: " + value+"Stamp:"+stamp);
                // Thread2通过CAS再修改value值为1
                if (atomicStamped.compareAndSet(value, 1,stamp,stamp+1)) {
                    System.out.println("Thread2 update from " + value + " to 1");
                }
            } else {
                System.out.println("Thread2 update fail!");
            }
        }, "Thread2").start();
    }

    private static void aba() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        new Thread(() -> {
           int value = atomicInteger.get();
           System.out.println("Thread1 read value: " + value);
           LockSupport.parkNanos(1000000000L);
           // Thread1通过CAS修改value值为3
           if (atomicInteger.compareAndSet(value, 3)) {
               System.out.println("Thread1 update from " + value + " to 3");
           } else {
               System.out.println("Thread1 update fail!");
           }
       }, "Thread1").start();

        new Thread(() -> {
           int value = atomicInteger.get();
           System.out.println("Thread2 read value: " + value);
           // Thread2通过CAS修改value值为2
           if (atomicInteger.compareAndSet(value, 2)) {
               System.out.println("Thread2 update from " + value + " to 2");
               value = atomicInteger.get();
               System.out.println("Thread2 read value: " + value);
               // Thread2通过CAS再修改value值为1
               if (atomicInteger.compareAndSet(value, 1)) {
                   System.out.println("Thread2 update from " + value + " to 1");
               }
           } else {
               System.out.println("Thread2 update fail!");
           }
       }, "Thread2").start();
    }
}
