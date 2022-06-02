package com.example.commontool.javaconcurrent.juc;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ScheduleExecutorTest
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/30
 **/
public class ScheduleExecutorTest {
    public static void main(String[] args) {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);
        // 只执行一次
        //scheduledThreadPool.schedule(new Task(),2, TimeUnit.SECONDS);
        // 从开始执行时算间隔周期
        //scheduledThreadPool.scheduleAtFixedRate(new Task(),1,5,TimeUnit.SECONDS);
        // 任务完成后开始算间隔周期
        scheduledThreadPool.scheduleWithFixedDelay(new Task(),1,5,TimeUnit.SECONDS);

        int got=2;

    }

    public static class Task implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(LocalDateTime.now().toString()+"---------task.run()...-----------");
        }
    }
}
