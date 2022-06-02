package com.example.commontool.javaconcurrent.juc;

import io.reactivex.Completable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ThreadAndPoolCompareTest
 * @Description 线程与线程池比较
 * @Author tianhuan
 * @Date 2022/5/30
 **/
public class ThreadAndPoolCompareTest {
    public static void main(String[] args) {
      //  threadExecute(10000);
        poolExecute(10000);

        // 线程池执行大大快于多线程，CPU密集型任务执行时间快，线程复用，避免CPU上下文切换，大大提升执行效率

    }

    public static void threadExecute(int cycle) {
        Long start = System.currentTimeMillis();
        final Random random = new Random();
        final List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < cycle; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    list.add(random.nextInt());
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("threadExecute时间：" + (System.currentTimeMillis()-start));
        System.out.println("threadExecute大小：" + list.size());
    }

    public static void poolExecute(int cycle) {
        Long start = System.currentTimeMillis();
        final Random random = new Random();
        final List<Integer> list = new ArrayList<Integer>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < cycle; i++) {
            executorService.execute(()->{
                list.add(random.nextInt());
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("poolExecute时间：" + (System.currentTimeMillis()-start));
        System.out.println("poolExecute大小：" + list.size());
    }
}
