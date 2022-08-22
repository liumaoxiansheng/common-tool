package com.example.commontool.javaconcurrent.juc.sync;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BatchBasicLockTest
 * @Description 偏向锁批量
 * @Author tianhuan
 * @Date 2022/6/8
 **/
@Slf4j
public class BatchBasicLockTest {
    public static void main(String[] args) throws Exception {

        // 等待延时偏向锁生效
        Thread.sleep(5000);

        List<Object> list = new ArrayList<>();

        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                Object lock = new Object();
                synchronized (lock) {
                    list.add(lock);
                }
            }
            try {
                // 线程保活
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "Thread1").start();

        // 睡眠3S，保证对象创建完成
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //
        log.debug("打印thead1，list中第20个对象的对象头：");
        log.debug((ClassLayout.parseInstance(list.get(19)).toPrintable()));
        // 线程2
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                Object obj = list.get(i);
                synchronized (obj) {
                    if (i >= 15 && i <= 21 || i >= 38) {
                        log.debug("thread2‐第" + (i + 1) + "次加锁执行中\t" + ClassLayout.parseInstance(obj).toPrintable());
                    }
                }
                if (i == 17 || i == 19) {
                    log.debug("thread2‐第" + (i + 1) + "次释放锁\t" + ClassLayout.parseInstance(obj).toPrintable());
                }
            }
            try {
                Thread.sleep(100000);

            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }, "thead2").start();

        Thread.sleep(3000);
        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                Object lock = list.get(i);
                if (i >= 17 && i <= 21 || i >= 35 && i <= 41) {
                    log.debug("thread3‐第" + (i + 1) + "次准备加锁\t" + ClassLayout.parseInstance(lock).toPrintable());
                }
                synchronized (lock) {
                    if (i >= 17 && i <= 21 || i >= 35 && i <= 41) {
                        log.debug("thread3‐第" + (i + 1) + "次加锁执行中\t" + ClassLayout.parseInstance(lock).toPrintable());
                    }
                }
            }
        }, "thread3").start();
        Thread.sleep(3000);
        log.debug("查看新创建的对象");
        log.debug((ClassLayout.parseInstance(new Object()).toPrintable()));

    }
}
