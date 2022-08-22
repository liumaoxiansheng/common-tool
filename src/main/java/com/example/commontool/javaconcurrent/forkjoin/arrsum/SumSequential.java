package com.example.commontool.javaconcurrent.forkjoin.arrsum;

import com.example.commontool.javaconcurrent.forkjoin.Utils;

import java.time.Duration;
import java.time.Instant;

/**
 * @author th_legend
 *
 * 单线程计算1亿个整数的和
 */
public class SumSequential {
    public static long sum(int[] arr){
        return SumUtils.sumRange(arr, 0, arr.length);
    }

    public static void main(String[] args) {
        // 准备数组
        int[] arr = Utils.buildRandomIntArray(100000000);
        System.out.printf("The array length is: %d\n", arr.length);
        Instant now = Instant.now();
        //数组求和
        long result = sum(arr);
        System.out.println("执行时间："+Duration.between(now,Instant.now()).toMillis());

        System.out.printf("The result is: %d\n", result);
    }
}
