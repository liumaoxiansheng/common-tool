package com.example.commontool.javaconcurrent.juc.atomic;

import lombok.Data;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.*;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

/**
 * @ClassName AtomicTest
 * @Description 原子类测试
 * @Author tianhuan
 * @Date 2022/6/6
 **/
public class AtomicTest {

    static int[] value = new int[]{1, 2, 3, 4, 5};
    static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(value);


    public static void main(String[] args) {
        // 原子数组操作
        // atomicArrayTest();、
        // 原子对象操作
        // atomicReferenceTest();
        // 原子属性修改器操作
        // atomicUpdaterTest();
        // 累加器操作LongAdder/DoubleAdder
        // adderTest();
        // LongAccumulator 操作
        // 累加 x+y
        LongAccumulator accumulator = new LongAccumulator(new MyAccumulator(), 0);
        ExecutorService executor = Executors.newFixedThreadPool(8);
        // 1到9累加
        for (int i = 0; i < 100; i++) {
            final int j=i;
            executor.submit(() -> accumulator.accumulate(j));
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(accumulator.getThenReset());
        executor.shutdown();

    }

    private static class MyAccumulator implements LongBinaryOperator{

        @Override
        public long applyAsLong(long left, long right) {
            return left+right;
        }
    }

    private static void adderTest() {
        testAtomicLongVSLongAdder(10, 10000);
        System.out.println("==================");
        testAtomicLongVSLongAdder(10, 200000);
        System.out.println("==================");
        testAtomicLongVSLongAdder(1000, 2000000);
    }

    static void testAtomicLongVSLongAdder(final int threadCount, final int times) {
        try {
            long start = System.currentTimeMillis();
            testLongAdder(threadCount, times);
            long end = System.currentTimeMillis() - start;
            System.out.println("条件>>>>>>线程数:" + threadCount + ", 单线程操作计数" + times);
            System.out.println("结果>>>>>>LongAdder方式增加计数" + (threadCount * times) + "次,共计耗时:" + end);
            long start2 = System.currentTimeMillis();
            testAtomicLong(threadCount, times);
            long end2 = System.currentTimeMillis() - start2;
            System.out.println("条件>>>>>>线程数:" + threadCount + ", 单线程操作计数" + times);
            System.out.println("结果>>>>>>AtomicLong方式增加计数" + (threadCount * times) + "次,共计耗时:" + end2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void testAtomicLong(final int threadCount, final int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicLong atomicLong = new AtomicLong();
        for (int i = 0; i < threadCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < times; j++) {
                        atomicLong.incrementAndGet();

                    }
                    countDownLatch.countDown();

                }
            }, "my‐thread" + i).start();
        }
        countDownLatch.await();

    }

    static void testLongAdder(final int threadCount, final int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        LongAdder longAdder = new LongAdder();
        for (int i = 0; i < threadCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < times; j++) {
                        longAdder.add(1);

                    }
                    countDownLatch.countDown();

                }
            }, "my‐thread" + i).start();
        }
        countDownLatch.await();

    }


    private static void atomicUpdaterTest() {
        AtomicIntegerFieldUpdater<Candide> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Candide.class, "score");
        Candide candide = new Candide();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int i1 = 0; i1 < 100; i1++) {
                    candide.score2.incrementAndGet();
                    fieldUpdater.incrementAndGet(candide);
                }
            }, "Thread" + i).start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(candide);
    }

    private static void atomicReferenceTest() {
        User jack = new User("jack", 18);
        User mark = new User("mark", 20);
        User james = new User("james", 25);
        AtomicReference<User> userAtomicReference = new AtomicReference<>();
        userAtomicReference.set(jack);
        // 比较赋值mark
        userAtomicReference.compareAndSet(jack, mark);
        System.out.println("userAtomicReference.get() = " + userAtomicReference.get());

        // 比较赋值james
        userAtomicReference.compareAndSet(jack, james);
        System.out.println("userAtomicReference.get() = " + userAtomicReference.get());
    }

    private static void atomicArrayTest() {
        atomicIntegerArray.set(0, 123);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int i1 = 0; i1 < 100; i1++) {
                    atomicIntegerArray.getAndAdd(0, 2);
                }
            }, "Thread" + i).start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(atomicIntegerArray.get(0));
    }

    @Data
    public static class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.age = age;
            this.name = name;
        }
    }

    @Data
    public static class Candide {
        volatile int score = 0;

        AtomicInteger score2 = new AtomicInteger(0);
    }
}
