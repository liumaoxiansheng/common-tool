package com.example.commontool.algorithms;

import jdk.nashorn.internal.ir.IfNode;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @ClassName Test
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/6/18
 **/
public class Test {

    /**
     * 两个栈实现出队入队操作
     **/
    static Stack<Integer> s1 = new Stack<>();
    static Stack<Integer> s2 = new Stack<>();

    public static void main(String[] args) {
        int[] arr={1,2,5,7,9,2,2};
        Stack<Integer> stack = doPushVal(arr);
        while (!stack.isEmpty()){
            System.out.println(stack.pop());
        }
        //Collections.sort();
    }

    /****/
    public static Stack<Integer> doPushVal(int[] arr) {
        Stack<Integer> stack = new Stack<>();
        for (int val : arr) {
                doPush(stack, val);
        }

        return stack;

    }

    public static void doPush(Stack<Integer> stack, Integer val) {
        // 空栈，则直接入栈
        if (stack.isEmpty()) {
            stack.push(val);
            return;
        }
        Integer next = stack.pop();

        if (next.equals(val)) {
            // 满足条件，当前值乘以2继续入栈操作
            val = 2 * val;
            doPush(stack, val);
        } else {
            int curVal = 0;
            if (stack.isEmpty()) {
                // 表示没有满足条件的,重新放入
                stack.push(next);
                stack.push(val);
            } else {
                // 计算是否有满足的值
                curVal +=next;
                LinkedList<Integer> cache = new LinkedList<>();
                cache.add(next);
                boolean findVal = false;
                while (!stack.isEmpty()) {
                    next = stack.pop();
                    curVal +=next;
                    if (curVal == val) {
                        findVal = true;
                        break;
                    }
                    cache.push(next);
                }
                if (findVal) {
                    cache.clear();
                    doPush(stack, curVal * 2);
                } else {
                    cache.add(val);
                    if (!cache.isEmpty()){
                        for (Integer v : cache) {
                            stack.push(v);
                        }
                    }
                }
            }
        }
    }


    /**
     * 给你一个正整数 n ，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的 n x n 正方形矩阵 matrix
     **/
    public int[][] generateMatrix(int n) {
        return null;

    }


    /**
     * 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
     * <p>
     * 字符串的一个子序列是原始字符串删除一些（也可以不删除）字符而不改变剩余字符相对位置形成的新字符串。（例如，"ace"是"abcde"的一个子序列，而"aec"不是）。
     **/
    public static boolean isSubsequence(String s, String t) {
        int sidx = 0, tidx = 0;
        if (s == null || s.length() == 0) {
            return true;
        }

        if (t == null || t.length() == 0) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            while (tidx < t.length()) {
                if (s.charAt(i) == t.charAt(tidx++)) {
                    sidx++;
                    break;
                }
            }
        }
        return sidx == s.length();

    }

    /**
     * 给你一个下标从 0 开始的字符串 word 和一个字符 ch 。找出 ch 第一次出现的下标 i ，反转 word 中从下标 0 开始、直到下标 i 结束（含下标 i ）的那段字符。如果 word 中不存在字符 ch ，则无需进行任何操作。
     */
    public static String reversePrefix(String word, char ch) {
        if (word == null || word.length() == 0) {
            return word;
        }
        int index = word.indexOf(ch);
        if (index < 0) {
            return word;
        }
        String tail = word.substring(index + 1);
        StringBuilder head = new StringBuilder();
        for (int i = index; i > 0; i--) {
            head.append(word.charAt(i));
        }
        head.append(tail);
        return head.toString();

    }

    static void enQueue(Integer i) {
        s1.push(i);
    }

    static int deQueue() {
        // 出队
        while (!s1.isEmpty()) {
            s2.push(s1.pop());
        }
        int result = s2.pop();
        // 倒回s1
        while (!s2.isEmpty()) {
            s1.push(s2.pop());
        }
        return result;
    }


    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        if (n == 0) {
            return;
        }

        int[] tmp = new int[m + n];
        int idx1 = 0, idx2 = 0;
        while (idx1 < m || idx2 < n) {
            if (idx1 == m) {
                tmp[idx1 + idx2] = nums2[idx2++];
                continue;
            }
            if (idx2 == n) {
                tmp[idx1 + idx2] = nums1[idx1++];
                continue;
            }

            if (nums1[idx1] > nums2[idx2]) {
                tmp[idx1 + idx2] = nums2[idx2++];
            } else {
                tmp[idx1 + idx2] = nums1[idx1++];
            }
        }
        for (int i = 0; i < tmp.length; i++) {
            nums1[i] = tmp[i];
        }

    }


    /**
     * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
     * <p>
     * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
     *
     * @param n:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/23
     **/
    public static int climbStairs(int n) {
        int[] cache = new int[n + 1];
        return climbStairs(n, cache);
    }

    public static int climbStairs(int n, int[] cache) {
        if (cache[n] > 0) {
            return cache[n];
        }
        if (n == 1 || n == 2) {
            cache[n] = n;
        } else {
            cache[n] = climbStairs(n - 1, cache) + climbStairs(n - 2, cache);
        }
        return cache[n];

    }


    public static boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        // 计算高度差
        return getHeight(root) >= 0;


    }

    public static int getHeight(TreeNode root) {
        if (root == null) {
            return 0;
        }
        TreeNode right = root.right;
        TreeNode left = root.left;
        // 计算高度
        int rh = getHeight(right);
        int lh = getHeight(left);
        // 高度差大于1则非平衡，返回-1；
        if (Math.abs(rh - lh) <= 1 && rh >= 0 && lh >= 0) {
            return Math.max(rh, lh) + 1;
        }
        return -1;

    }


    /**
     * 给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验这两棵树是否相同。
     * <p>
     * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
     *
     * @param p:
     * @param q:
     * @return boolean
     * @Author tianhuan
     * @Date 2022/6/27
     **/
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }


    /**
     * 给定一个非负整数 numRows，生成「杨辉三角」的前 numRows 行。
     * <p>
     * 在「杨辉三角」中，每个数是它左上方和右上方的数的和。
     *
     * @param numRows:
     * @return java.util.List<java.util.List < java.lang.Integer>>
     * @Author tianhuan
     * @Date 2022/6/29
     **/
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> res = new ArrayList<>(numRows);
        // 第一行
        LinkedList<Integer> row;
        row = new LinkedList<>();
        row.add(1);
        res.add(row);
        //
        if (numRows > 1) {

        }
        return res;

    }

    /**
     * 给你二叉树的根节点root 和一个表示目标和的整数targetSum 。
     * 判断该树中是否存在根节点到叶子节点的路径，这条路径上所有节点值相加等于目标和targetSum 。
     * 如果存在，返回 true ；否则，返回 false 。
     *
     * @param root:
     * @param targetSum:
     * @return boolean
     * @Author tianhuan
     * @Date 2022/6/29
     **/
    public boolean hasPathSum(TreeNode root, int targetSum) {

        if (root == null) {
            return false;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                TreeNode node = queue.poll();
                // 是叶子点
                if (node.left == null && node.right == null) {
                    if (node.val == targetSum) {
                        return true;
                    }
                }
                // 非子节点则赋值向下传递
                if (node.left != null) {
                    node.left.val = node.val + node.left.val;
                    queue.offer(node.left);
                }

                if (node.right != null) {
                    node.right.val = node.val + node.right.val;
                    queue.offer(node.right);
                }
            }
        }
        return false;
    }


    public boolean hasPathSumDG(TreeNode root, int targetSum) {

        if (root == null) {
            return false;
        }
        // 是叶子点
        if (root.left == null && root.right == null) {
            if (root.val == targetSum) {
                return true;
            }
        }

        return hasPathSum(root.left, targetSum - root.val) || hasPathSum(root.right, targetSum - root.val);
    }

    /**
     * 最小深度
     *
     * @param root:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/29
     **/
    public static int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right != null) {
            return minDepth(root.right) + 1;
        }

        if (root.right == null && root.left != null) {
            return minDepth(root.left) + 1;
        }

        return Math.min(minDepth(root.left), minDepth(root.right)) + 1;

    }

    public static int minDepth2(TreeNode root) {
        int dept = 0;
        if (root == null) {
            return dept;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            dept++;
            // 当前深度的节点个数，遍历判断
            int size = queue.size();
            while (size-- > 0) {
                TreeNode node = queue.poll();
                // 如果没有节点返回该深度
                if (node.left == null && node.right == null) {
                    return dept;
                }

                if (node.left != null) {
                    queue.offer(node.left);
                }

                if (node.right != null) {
                    queue.offer(node.right);
                }

            }
        }

        return -1;
    }


    /**
     * 求二叉树的最大深度
     *
     * @param root:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/27
     **/
    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return maxDepth(root.left, root.right) + 1;

    }

    public static int maxDepth(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return 0;
        }
        if (left == null) {
            return maxDepth(right.left, right.right) + 1;
        }
        if (right == null) {
            return maxDepth(left.left, left.right) + 1;
        }
        return Math.max(maxDepth(left.left, left.right), maxDepth(right.left, right.right)) + 1;
    }


    /**
     * 有序数组转化位二叉平衡树
     *
     * @param nums:
     * @return com.example.commontool.algorithms.Test.TreeNode
     * @Author tianhuan
     * @Date 2022/6/28
     **/
    public TreeNode sortedArrayToBST(int[] nums) {
        // 每次中间节点为根节点
        int right = nums.length;
        int left = 0;
        return sortedArrayToBST(nums, left, right);
    }

    public static TreeNode sortedArrayToBST(int[] nums, int left, int right) {
        // 每次中间节点为根节点
        int mid = left + (right - left) / 2;
        // 终止条件,分割的区间没有则表示没有子节点
        if (left >= right) {
            return null;
        }

        TreeNode node = new TreeNode(nums[mid]);
        node.left = sortedArrayToBST(nums, left, mid);
        node.right = sortedArrayToBST(nums, mid + 1, right);
        return node;
    }


    /**
     * 判断两个二叉树是否是对称二叉树
     *
     * @param root:
     * @return boolean
     * @Author tianhuan
     * @Date 2022/6/27
     **/
    public static boolean isSymmetric(TreeNode root) {
        // 递归
        if (root == null) {
            return true;
        }
        return isSymmetric(root.left, root.right);
    }

    public static boolean isSymmetric(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        if (left.val != right.val) {
            return false;
        }
        return isSymmetric(left.left, right.right) && isSymmetric(left.right, right.left);
    }

    /**
     * 中序遍历:先左子树，再父节点，再右子树,如果左子树不为空
     *
     * @param root:
     * @return java.util.List<java.lang.Integer>
     * @Author tianhuan
     * @Date 2022/6/23
     **/
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new LinkedList<>();
        Stack<TreeNode> nodes = new Stack<>();
        while (root != null || !nodes.isEmpty()) {
            // 找到最左的左子树
            while (root != null) {
                nodes.push(root);
                root = root.left;
            }
            // 取出
            root = nodes.pop();
            list.add(root.val);
            // 右子树
            root = root.right;
        }

        return list;
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }


    /**
     * 给你一个非负整数 x ，计算并返回 x 的 算术平方根 。
     * <p>
     * 由于返回类型是整数，结果只保留 整数部分 ，小数部分将被 舍去 。
     *
     * @param x:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/23
     **/
    public static int mySqrt(int x) {
        if (x == 0 || x == 1) {
            return x;
        }
        // 迭代
        int min = 0;
        int max = x;
        while (min < max - 1) {
            int mid = (min + max) / 2;
            // mid^2>x===>还可以找
            if (x / mid < mid) {
                // 左边
                max = mid;
            } else {
                min = mid;
            }
        }
        return min;

    }


    /**
     * 二进制计算相加
     *
     * @param a:
     * @param b:
     * @return java.lang.String
     * @Author tianhuan
     * @Date 2022/6/23
     **/
    public static String addBinary(String a, String b) {
        int aIdx = a.length() - 1;
        int bIdx = b.length() - 1;
        char[] numArr = new char[a.length() > b.length() ? a.length() + 1 : b.length() + 1];
        // 进位
        int carry = 0;
        int idx = numArr.length - 1;
        while (aIdx >= 0 || bIdx >= 0) {
            int e1 = aIdx <= -1 ? 0 : a.charAt(aIdx--) == '1' ? 1 : 0;
            int e2 = bIdx <= -1 ? 0 : b.charAt(bIdx--) == '1' ? 1 : 0;
            int c = (e1 + e2 + carry) % 2;
            carry = (e1 + e2 + carry) / 2;
            numArr[idx--] = c == 0 ? '0' : '1';
        }
        if (carry > 0) {
            numArr[0] = '1';
        } else {
            return String.valueOf(numArr, 1, numArr.length - 1);
        }

        return String.valueOf(numArr);
    }


    /**
     * 给定一个由 整数 组成的 非空 数组所表示的非负整数，在该数的基础上加一。
     * 最高位数字存放在数组的首位， 数组中每个元素只存储单个数字。
     * 你可以假设除了整数 0 之外，这个整数不会以零开头。
     *
     * @param digits:
     * @return int[]
     * @Author tianhuan
     * @Date 2022/6/22
     **/
    public static int[] plusOne(int[] digits) {
        int length = digits.length;
        // 进位
        int b = 0;
        int tmp = digits[length - 1] + 1;
        if (tmp == 10) {
            b = 1;
            digits[length - 1] = 0;
        } else {
            digits[length - 1] = tmp;
            return digits;
        }

        for (int i = length - 2; i >= 0; i--) {
            int tmp2 = digits[i] + b;
            if (tmp2 == 10) {
                b = 1;
                digits[i] = 0;
            } else {
                b = 0;
                digits[i] = tmp2;
            }
        }
        if (b == 1) {
            // 高位进1，则需要创建
            int[] res = new int[digits.length + 1];
            System.arraycopy(digits, 0, res, 1, digits.length);
            res[0] = 1;
            return res;
        }
        return digits;
    }

    public static int lengthOfLastWord(String s) {
        int size = 0;
        for (int length = s.length() - 1; length >= 0; length--) {
            char c = s.charAt(length);
            if (size == 0 && c == ' ') {
                // 尾部的空格跳过
                continue;
            } else if (size > 0 && c == ' ') {
                break;
            } else {
                size++;
            }
        }
        return size;
    }

    /**
     * 最大子数组和
     *
     * @param nums:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/22
     **/
    public static int maxSubArray(int[] nums) {
        // 连续的值相加，大于0则连续相加，否则跳过，重新开始累加,每次循环需记录比较最大的值
        int cur = nums[0];
        int max = cur;
        for (int i = 1; i < nums.length; i++) {
            if (cur > 0) {
                cur = cur + nums[i];
            } else {
                cur = nums[i];
            }
            max = max > cur ? max : cur;
        }
        return max;
    }

    public static int maxSubArray2(int[] nums) {
        // 前面的元素大于1则加到当前元素并赋值，取最大值
        int cur = nums[0];
        int max = cur;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] > 0) {
                nums[i] = nums[i - 1] + nums[i];
            }
            max = max > nums[i] ? max : nums[i];
        }
        return max;
    }


    /**
     * 查找目标索引值
     *
     * @param nums:
     * @param target:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/22
     **/
    public static int searchInsert(int[] nums, int target) {
        int right = nums.length - 1;
        int left = 0;
        while (left <= right) {
            int mid = (right + left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                // right左移
                right = mid - 1;
            } else {
                // left右移
                left = mid + 1;
            }
        }
        return left;
    }


    /**
     * 实现子字符串第一次出现的位置
     *
     * @param haystack:
     * @param needle:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/22
     **/
    public static int strStr(String haystack, String needle) {
        int pre = 0;
        if (needle.length() == 0) {
            return pre;
        }
        // 遍历比较，父字符串开始
        for (int i = 0; i < haystack.length(); i++) {
            boolean flag = true;
            for (int j = 0; j < needle.length(); j++) {
                if (pre + j < haystack.length() && haystack.charAt(pre + j) == needle.charAt(j)) {
                    flag = true;
                } else {
                    pre++;
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return pre;
            }
        }

        return -1;
    }


    /**
     * 给你一个数组 nums和一个值 val，你需要 原地 移除所有数值等于val的元素，并返回移除后数组的新长度。
     * <p>
     * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
     * <p>
     * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
     *
     * @param nums:
     * @param val:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/21
     **/
    public static int removeElement(int[] nums, int val) {
        int count = 0;
        int pre = 0;// 前指针
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == val) {
                if (i + 1 < nums.length) {
                    nums[pre] = nums[i + 1];
                } else {
                    nums[pre] = -1;
                }
                count++;
            } else {
                nums[pre] = nums[i];
                pre++;
            }
        }
        return nums.length - count;
    }

    /**
     * 删除有序数组中的重复项
     *
     * @param nums:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/21
     **/
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 1) {
            return nums.length;
        }
        int pre = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[pre]) {
                nums[++pre] = nums[i];
            }

        }

        return ++pre;
    }


    /**
     * 给定一个只包括 '('，')'，'{'，'}'，'['，']'的字符串 s ，判断字符串是否有效。
     * <p>
     * 有效字符串需满足：
     * <p>
     * 左括号必须用相同类型的右括号闭合。
     * 左括号必须以正确的顺序闭合。
     *
     * @param s:
     * @return boolean
     * @Author tianhuan
     * @Date 2022/6/20
     **/
    public static boolean isValid(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(')');
            } else if (c == '[') {
                stack.push(']');
            } else if (c == '{') {
                stack.push('}');
            } else if (stack.isEmpty() || stack.pop() != c) {
                return false;
            }

        }
        return stack.isEmpty();
    }

    private static void testFindMedianSortedArrays() {
        int[] nums1 = {1, 3};
        int[] nums2 = {2};
        double v = findMedianSortedArrays(nums1, nums2);
        System.out.println("v = " + v);
    }


    /**
     * 最长回文字符串
     *
     * @param s:
     * @return java.lang.String
     * @Author tianhuan
     * @Date 2022/6/18
     **/
    public static String longestPalindrome(String s) {

        // 动态规划、中心扩散、manacher
        int len = s.length();
        if (len < 2) {
            return s;
        }

        int maxLen = 1;
        int begin = 0;
        // dp[i][j] 表示 s[i..j] 是否是回文串
        boolean[][] dp = new boolean[len][len];
        // 初始化：所有长度为 1 的子串都是回文串
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }

        char[] charArray = s.toCharArray();
        // 递推开始
        // 先枚举子串长度
        for (int L = 2; L <= len; L++) {
            // 枚举左边界，左边界的上限设置可以宽松一些
            for (int i = 0; i < len; i++) {
                // 由 L 和 i 可以确定右边界，即 j - i + 1 = L 得
                int j = L + i - 1;
                // 如果右边界越界，就可以退出当前循环
                if (j >= len) {
                    break;
                }

                if (charArray[i] != charArray[j]) {
                    dp[i][j] = false;
                } else {
                    if (j - i < 3) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }

                // 只要 dp[i][L] == true 成立，就表示子串 s[i..L] 是回文，此时记录回文长度和起始位置
                if (dp[i][j] && j - i + 1 > maxLen) {
                    maxLen = j - i + 1;
                    begin = i;
                }
            }
        }
        return s.substring(begin, begin + maxLen);
    }


    public static String longestPalindrome2(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }
        // 中间扩散法
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            // 奇数
            int len1 = expandAroundCenter(s, i, i);
            // 偶数
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            // 计算起始位置，最大长度
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    public static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            --left;
            ++right;
        }
        return right - left - 1;
    }

    /**
     * 最长公告前缀
     * 1、纵向扫描，比较每一列上的字符是否一样
     *
     * @param strs:
     * @return java.lang.String
     * @Author tianhuan
     * @Date 2022/6/20
     **/
    public static String longestCommonPrefix(String[] strs) {
        boolean flag = true;
        int idx = 0;
        while (flag) {
            boolean jg = true;
            int length = strs[0].length();
            if (length == 0) {
                return "";
            }
            if (idx < length) {
                char c = strs[0].charAt(idx);
                for (String str : strs) {
                    if (str.length() > idx) {
                        jg = jg && str.charAt(idx) == c;
                    } else {
                        jg = false;
                        break;
                    }
                }
            } else {
                jg = false;
            }

            if (jg) {
                idx++;
            }
            flag = jg;
        }

        return idx > 0 ? strs[0].substring(0, idx) : "";
    }


    /**
     * 罗马数转数字
     *
     * @param s:
     * @return int
     * @Author tianhuan
     * @Date 2022/6/20
     **/
    public static int romanToInt(String s) {
        s = s.replace("IV", "a");
        s = s.replace("IX", "b");
        s = s.replace("XL", "c");
        s = s.replace("XC", "d");
        s = s.replace("CD", "e");
        s = s.replace("CM", "f");
        // 罗马数字包含以下七种字符: I， V， X， L，C，D 和 M。
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case 'M':
                    res = res + 1000;
                    break;
                case 'D':
                    res = res + 500;
                    break;
                case 'C':
                    res = res + 100;
                    break;
                case 'L':
                    res = res + 50;
                    break;
                case 'X':
                    res = res + 10;
                    break;
                case 'V':
                    res = res + 5;
                    break;
                case 'I':
                    res = res + 1;
                    break;
                case 'a':
                    res = res + 4;
                    break;
                case 'b':
                    res = res + 9;
                    break;
                case 'c':
                    res = res + 40;
                    break;
                case 'd':
                    res = res + 90;
                    break;
                case 'e':
                    res = res + 400;
                    break;
                case 'f':
                    res = res + 900;
                    break;
                default:
                    return 0;
            }
        }

        return res;

    }

    /**
     * 判断是否是回文数
     *
     * @return boolean
     * @Author tianhuan
     * @Date 2022/6/20
     **/
    public static boolean isPalindrome(int x) {

        // 小于0或者是10的整数倍一定不是,0除外
        if (x < 0 || (x > 0 && x % 10 == 0)) {
            return false;
        }
        // 从低位取值，原数%10得到最低位，原数/10再%10得到倒数倒数第二位，然后最低为*10+倒数第二位得到两个长度的反正
        // 比如12  2 1 ====》2*10+1=21，然后递归就可以实现反转，为了提高效率和保证int的安全问题，只需要反正一半即可
        // 反转一半的终止条件即为反转的数大于剩下的另一半数
        int reverse = 0;
        while (x > reverse) {
            //反转一半
            reverse = reverse * 10 + x % 10;
            //位数减1
            x = x / 10;
        }
        // 数字长度为偶数则相等，为奇数则需要把中间位÷10去掉
        return x == reverse || x == reverse / 10;
    }


    public static boolean isPalindromeStr(String str) {
        // 中心扩散法
        if (str.length() == 1) {
            return true;
        }
        int midLeft = str.length() / 2;
        int midRight = str.length() / 2;
        if (str.length() % 2 == 0) {
            midLeft = str.length() / 2 - 1;
        }
        while (midLeft >= 0 && midRight <= str.length() - 1) {
            if (str.charAt(midLeft--) != str.charAt(midRight++)) {
                return false;
            }
        }
        return true;

    }

    public static boolean isPalindromeStr2(String str) {
        // 边界收缩法
        if (str.length() == 1) {
            return true;
        }
        int left = 0;
        int right = str.length() - 1;

        while (left <= right) {
            if (str.charAt(left++) != str.charAt(right--)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查找两个数组的中位数
     *
     * @param nums1:
     * @param nums2:
     * @return double
     * @Author tianhuan
     * @Date 2022/6/18
     **/
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int l1 = nums1.length;
        int l2 = nums2.length;
        int[] resArr = null;
        // 合并数组，排序
        int idx1 = 0;
        int idx2 = 0;
        if (l1 == 0) {
            resArr = nums2;
        } else if (l2 == 0) {
            resArr = nums1;
        } else {
            resArr = new int[l1 + l2];
            for (int i = 0; i < resArr.length; i++) {
                if (idx1 < l1 && idx2 < l2) {
                    // 比较大小
                    if (nums2[idx2] > nums1[idx1]) {
                        resArr[idx1 + idx2] = nums1[idx1];
                        idx1++;
                    } else {
                        resArr[idx1 + idx2] = nums2[idx2];
                        idx2++;
                    }
                } else {
                    // 一个数组的数据已经添加晚的情况
                    if (idx2 < l2) {
                        resArr[idx1 + idx2] = nums2[idx2];
                        idx2++;
                    } else if (idx1 < l1) {
                        resArr[idx1 + idx2] = nums1[idx1];
                        idx1++;
                    }
                }
            }
        }


        for (int i = 0; i < resArr.length; i++) {
            System.out.println(resArr[i]);
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
}
