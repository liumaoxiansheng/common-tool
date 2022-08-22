package com.example.commontool.javaconcurrent.forkjoin.arrsum;

import com.example.commontool.javaconcurrent.forkjoin.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SumRecursiveMT {
    public static class RecursiveSumTask implements Callable<Long> {
        //拆分的粒度
        public static final int SEQUENTIAL_CUTOFF = 100000;
        int lo;
        int hi;
        int[] arr; // arguments
        ExecutorService executorService;

        RecursiveSumTask(ExecutorService executorService, int[] a, int l, int h) {
            this.executorService = executorService;
            this.arr = a;
            this.lo = l;
            this.hi = h;
        }

        @Override
        public Long call() throws Exception {
            System.out.format("%s range [%d-%d] begin to compute %n",
                    Thread.currentThread().getName(), lo, hi);
            long result = 0;
            //最小拆分的阈值
            if (hi - lo <= SEQUENTIAL_CUTOFF) {
                for (int i = lo; i < hi; i++) {
                    result += arr[i];
                }
//                System.out.format("%s range [%d-%d] begin to finished %n",
//                        Thread.currentThread().getName(), lo, hi);
            } else {
                RecursiveSumTask left = new RecursiveSumTask(
                        executorService, arr, lo, (hi + lo) / 2);
                RecursiveSumTask right = new RecursiveSumTask(
                        executorService, arr, (hi + lo) / 2, hi);
                Future<Long> lr = executorService.submit(left);
                Future<Long> rr = executorService.submit(right);

                result = lr.get() + rr.get();
//                System.out.format("%s range [%d-%d] finished to compute %n",
//                        Thread.currentThread().getName(), lo, hi);
            }

            return result;
        }
    }


    public static long sum(int[] arr) throws Exception {

        //思考： 用 Executors.newFixedThreadPool可以吗？   定长线程的饥饿
       // ExecutorService executorService = Executors.newFixedThreadPool(12);
        ExecutorService executorService = Executors.newCachedThreadPool();
         //递归任务 求和
        RecursiveSumTask task = new RecursiveSumTask(executorService, arr, 0, arr.length);
        //返回结果
        long result = executorService.submit(task).get();

        executorService.shutdown();
        return result;
    }

    public static void main(String[] args) throws Exception {
        //准备数组
        int[] arr = Utils.buildRandomIntArray(100000000);
        System.out.printf("The array length is: %d\n", arr.length);
        Instant now = Instant.now();
        //数组求和
        long result = sum(arr);
        System.out.println("执行时间："+ Duration.between(now,Instant.now()).toMillis());
        System.out.printf("The result is: %d\n", result);

    }
}