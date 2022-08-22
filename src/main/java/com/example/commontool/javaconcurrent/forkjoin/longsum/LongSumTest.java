package com.example.commontool.javaconcurrent.forkjoin.longsum;

import com.example.commontool.javaconcurrent.forkjoin.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.IntStream;

/**
 * @ClassName LongSumTest
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/6/16
 **/
public class LongSumTest {
    // 获取逻辑处理器数量 12
    static final int NCPU = Runtime.getRuntime().availableProcessors();

    static long calcSum;


    public static void main(String[] args) throws Exception {
        //准备数组
        int[] array = Utils.buildRandomIntArray(100000000);

        Instant now = Instant.now();
        // 单线程计算数组总和
        calcSum = seqSum(array);
        System.out.println("seq sum=" + calcSum);
        System.out.println("执行时间："+ Duration.between(now,Instant.now()).toMillis());

        //递归任务
        LongSum ls = new LongSum(array, 0, array.length);
        // 构建ForkJoinPool
        ForkJoinPool fjp  = new ForkJoinPool(NCPU);




        now = Instant.now();
        //ForkJoin计算数组总和
        ForkJoinTask<Long> result = fjp.submit(ls);
        System.out.println("forkjoin sum=" + result.get());
        System.out.println("执行时间："+ Duration.between(now,Instant.now()).toMillis());

        fjp.shutdown();

        now = Instant.now();
        //并行流计算数组总和
        Long sum = (Long) IntStream.of(array).asLongStream().parallel().sum();
        System.out.println("IntStream sum="+sum);
        System.out.println("执行时间："+ Duration.between(now,Instant.now()).toMillis());

    }


    static long seqSum(int[] array) {
        long sum = 0;
        for (int i = 0; i < array.length; ++i) {
            sum += array[i];
        }
        return sum;
    }
}
