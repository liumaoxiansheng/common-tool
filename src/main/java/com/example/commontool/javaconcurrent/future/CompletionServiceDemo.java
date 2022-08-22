package com.example.commontool.javaconcurrent.future;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * @author th_legend
 * 电商询价业务
 */
@Slf4j
public class CompletionServiceDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        //创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //创建CompletionService
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        //异步向电商S1询价
        cs.submit(() -> getPriceByS1());
        //异步向电商S2询价
        cs.submit(() -> getPriceByS2());
        //异步向电商S3询价
        cs.submit(() -> getPriceByS3());
        //将询价结果异步保存到数据库
        for (int i = 0; i < 3; i++) {
            //从阻塞队列获取futureTask
            Integer r = cs.take().get();
            executor.execute(() -> save(r));
        }


        executor.shutdown();
    }

    private static void save(Integer r) {
        log.debug("保存询价结果:{}",r);
    }

    private static Integer getPriceByS1() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(5000);
        log.debug("电商S1询价信息1200");
        return 1200;
    }
    private static Integer getPriceByS2() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(8000);
        log.debug("电商S2询价信息1000");
        return 1000;
    }
    private static Integer getPriceByS3()  throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(3000);
        log.debug("电商S3询价信息800");
        return 800;
    }
}
