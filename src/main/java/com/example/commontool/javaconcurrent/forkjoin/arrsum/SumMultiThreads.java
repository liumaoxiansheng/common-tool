package com.example.commontool.javaconcurrent.forkjoin.arrsum;


import com.example.commontool.javaconcurrent.forkjoin.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author th_legend
 *
 * 多线程计算1亿个整数的和
 */
public class SumMultiThreads {
    //拆分的粒度
    public final static int NUM = 10000000;

    public static long sum(int[] arr, ExecutorService executor) throws Exception {
        long result = 0;
        int numThreads = arr.length / NUM > 0 ? arr.length / NUM : 1;
        int num = arr.length / numThreads;
        //任务分解
        SumTask[] tasks = new SumTask[numThreads];
        Future<Long>[] sums = new Future[numThreads];
        for (int i = 0; i < numThreads; i++) {
            tasks[i] = new SumTask(arr, (i * NUM),
                    ((i + 1) * NUM));
            sums[i] = executor.submit(tasks[i]);
        }
        //结果合并
        for (int i = 0; i < numThreads; i++) {
            result += sums[i].get();
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        // 准备数组
        int[] arr = Utils.buildRandomIntArray(100000000);
        //获取线程数
        int numThreads = arr.length / NUM > 0 ? arr.length / NUM : 1;

        System.out.printf("The array length is: %d\n", arr.length);
        // 构建线程池
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        //预热
        //((ThreadPoolExecutor)executor).prestartAllCoreThreads();

        Instant now = Instant.now();
        // 数组求和
        long result = sum(arr, executor);
        System.out.println("执行时间："+Duration.between(now,Instant.now()).toMillis());

        System.out.printf("The result is: %d\n", result);

        executor.shutdown();

    }
}
