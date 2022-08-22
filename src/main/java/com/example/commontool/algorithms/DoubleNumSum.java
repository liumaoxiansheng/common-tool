package com.example.commontool.algorithms;

import java.util.*;

/**
 * @ClassName DoubleNumSum
 * @Description 两数之和
 * @Author tianhuan
 * @Date 2022/6/17
 **/
public class DoubleNumSum {
    // 给定一个整数数组 num和一个整数目标值 target，请你在该数组中找出 和为目标值 target 的那两个整数，并返回它们的数组下标。
    //
    //你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
    //
    //你可以按任意顺序返回答案。
    public static void main(String[] args) {
        int[] nums = {2, 15, 7, 11};
        int target = 18;
        int[] res = arrayAlgo(nums, target);
        for (int re : res) {
            System.out.println(re);
        }


    }

    /**
     * 求最长不重复字符的字符串的长度
     *
     * @param s:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/17
     **/
    public int lengthOfLongestSubstring(String s) {
        // 哈希集合，记录每个字符是否出现过
        Set<Character> occ = new HashSet<Character>();
        int n = s.length();
        // 右指针，初始值为 -1，相当于我们在字符串的左边界的左侧，还没有开始移动
        int rk = -1, ans = 0;
        for (int i = 0; i < n; ++i) {
            if (i != 0) {
                // 左指针向右移动一格，移除一个字符
                occ.remove(s.charAt(i - 1));
            }
            while (rk + 1 < n && !occ.contains(s.charAt(rk + 1))) {
                // 不断地移动右指针
                occ.add(s.charAt(rk + 1));
                ++rk;
            }
            // 第 i 到 rk 个字符是一个极长的无重复字符子串
            ans = Math.max(ans, rk - i + 1);
        }
        return ans;
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int l1 = nums1.length;
        int l2 = nums2.length;
        int[] resArr = new int[l1+l2];
        int idx2=0;
        // 合并数组，排序
        for (int i = 0; i < nums1.length; i++) {
            for (int j = idx2; j < nums2.length; j++) {
                if (nums2[j]>nums1[i]){
                   resArr[i+j]=nums1[i];
                }else {
                    resArr[i+j]=nums2[j];
                    idx2=j+1;
                    break;
                }
            }
        }


        int length = resArr.length;
        if (length % 2 == 0) {
            //偶数个，中位数取中间两个数的平均
            return (resArr[(length / 2)] + resArr[(length / 2 - 1)]) / 2.0;
        } else {
            //基数个，中位数等于中间元素
            return resArr[(length / 2)];
        }


    }


    /**
     * 利用减法:
     * <p>
     * 1、target-元素值====》一个容器中
     * 2、遍历过程中,后续的元素判断是否存在容器中,存在则该元素下标和容器元素对应的下标即为解
     *
     * @param nums:
     * @param target:
     * @return int[]
     * @Author tianhuan
     * @Date 2022/6/17
     **/

    public static int[] arrayAlgo(int[] nums, int target) {
        int[] res = new int[2];
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < nums.length; i++) {
            if (list.contains(nums[i])) {
                res[0] = list.indexOf(nums[i]);
                res[1] = i;
                break;
            }
            list.add(target - nums[i]);
        }
        return res;

    }

    public static int[] hashAlgo(int[] nums, int target) {
        int[] res = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                res[0] = map.get(nums[i]);
                res[1] = i;
                break;
            }
            map.put(target - nums[i], i);
        }
        return res;

    }

    /**
     * 暴力解法:双重for循环，穷举两两组合，进行匹配，时间复杂度O(n^2)
     *
     * @param nums:
     * @param target:
     * @return int[]
     * @Author tianhuan
     * @Date 2022/6/17
     **/
    public static int[] forceAlgo(int[] nums, int target) {
        int[] res = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    res[0] = i;
                    res[1] = j;
                    break;
                }
            }
        }
        return res;

    }
}
