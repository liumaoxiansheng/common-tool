package com.example.commontool.netty.nettyadv.server.asyntask;

import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @ClassName BusinessProcessor
 * @Description 业务执行器
 * @Author tianhuan
 * @Date 2022/5/19
 **/
@Slf4j
public class BusinessProcessor {


    private static BlockingQueue<Runnable> taskQueue  = new ArrayBlockingQueue<Runnable>(3000);
    private static ExecutorService executorService = new ThreadPoolExecutor(1,
            NettyRuntime.availableProcessors(),60, TimeUnit.SECONDS,taskQueue);

    public static void submitTask(Runnable task){
        executorService.execute(task);
    }
}
