package com.example.commontool.javaconcurrent.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureDemo {

    public static void main(String[] args) {


        ExecutorService executorService = Executors.newFixedThreadPool(10);

        log.debug("monkey进入餐厅，点了份西红柿炒番茄");
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(()->{
            log.debug("厨师炒菜");
            sleep(2,TimeUnit.SECONDS);
            return "西红柿炒番茄好了";
        },executorService).thenCombine(CompletableFuture.supplyAsync(()->{
            log.debug("服务员蒸饭");
            sleep(3,TimeUnit.SECONDS);
            return "米饭好了";
        }),(dish,rice)->{
            log.debug("服务员打饭");
            sleep(1,TimeUnit.SECONDS);
            return dish+","+rice;
        });

        log.debug("monkey在刷抖音");
        log.debug("{},monkey开吃",cf.join());



        log.debug("monkey吃完饭去结账，要求开发票");
        cf = CompletableFuture.supplyAsync(()->{
            log.debug("服务员收款");
            sleep(1,TimeUnit.SECONDS);
            return "20";
        }).thenApply(money->{
            log.debug("服务员开发票，面额{}元",money);
            sleep(2,TimeUnit.SECONDS);
            return money+"元发票";
        });

        log.debug("monkey接到朋友电话");
        log.debug("monkey拿到{}，准备回家", cf.join());


        log.debug("monkey走出餐厅，来到公交车站，等待301路或者918路公交到来");
        cf = CompletableFuture.supplyAsync(() -> {
            log.debug("301路公交正在赶来");
            sleep(2,TimeUnit.SECONDS);
            return "301路到了";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            log.debug("918路公交正在赶来");
            sleep(3,TimeUnit.SECONDS);
            return "918路到了";
        }), bus -> {
            if(bus.startsWith("918")){
                throw new RuntimeException("918撞树了.......");
            }
            return bus;
        }).exceptionally(e->{
            log.debug(e.getMessage());
            log.debug("monkey叫出租车");
            sleep(3,TimeUnit.SECONDS);
            return "出租车到了";
        });

        log.debug("{},monkey坐车回家", cf.join());

        executorService.shutdown();


    }

    static void sleep(int t, TimeUnit u){
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
        }
    }
}