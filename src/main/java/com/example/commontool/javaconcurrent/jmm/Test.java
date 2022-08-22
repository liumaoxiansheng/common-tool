package com.example.commontool.javaconcurrent.jmm;

/**
 * @ClassName Test
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/25
 **/
public class Test {

    private volatile  int i1=0;
    private   int i2=0;

    public static void main(String[] args) {
        new Test().testCpu();
//        int hash=1566615;
//        int n=256;
//        System.out.println(hash%n);
//        System.out.println((n - 1) & hash);
    }

    public void testCpu(){
        long s = System.currentTimeMillis();
        // int count=0;
        while (i1<=10000000) {
            i1++;
        }
        long e = System.currentTimeMillis();
        System.out.println("volatile耗时: "+(e-s));

        // volatile保证可见性，每次都是从主内存读取数据，所以相对慢

        System.out.println("-----------------");
        long s1 = System.currentTimeMillis();
        // int count=0;
        while (i2<=10000000) {
            i2++;
        }
        long e1 = System.currentTimeMillis();

        System.out.println("int耗时: "+(e1-s1));
    }
}
