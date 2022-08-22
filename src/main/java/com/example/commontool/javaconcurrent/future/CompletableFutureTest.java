package com.example.commontool.javaconcurrent.future;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CompletableFutureTest
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/6/20
 **/
@Slf4j
public class CompletableFutureTest {
    public static void main(String[] args) {
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        log.debug("进入饭馆，点了份猪脚饭....");
//        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
//            sleep(1, TimeUnit.SECONDS);
//            return "老板做好了饭...,端了过来";
//        }, executorService).thenApply((s) -> {
//            sleep(2, TimeUnit.SECONDS);
//            return s + "，准备拿筷子吃饭";
//        });
//        cf.thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
//            sleep(2, TimeUnit.SECONDS);
//            return ",边看会新闻";
//
//        }), (rice, news) -> {
//            sleep(1, TimeUnit.SECONDS);
//            log.debug(rice + news + ",结账走人....");
//        });
//        log.debug("看会儿手机....");
//        log.debug(cf.join());
        Random random = new Random();
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(random.nextInt(4),TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "hello";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(random.nextInt(3),TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "world";
        });
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                sleep(random.nextInt(2),TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "hello,world,555";
        });

        CompletableFuture<Object> result = CompletableFuture.anyOf(future1, future2,future3);
        log.debug(result.join().toString());
    }

    static void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
        }
    }
}
