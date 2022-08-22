package com.example.commontool.javaconcurrent.forkjoin.arrsum;

public class SumUtils {

    /**
     * 数组求和
     * @param arr
     * @param lo
     * @param hi
     * @return
     */
    public static long sumRange(int[] arr, int lo, int hi) {
        long result = 0;

        for (int j = lo; j < hi; j++) {
            result += arr[j];
        }
        return result;
    }

}
