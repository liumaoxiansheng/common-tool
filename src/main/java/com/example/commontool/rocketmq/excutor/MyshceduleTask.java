package com.example.commontool.rocketmq.excutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyshceduleTask {
    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.printf("executor...current time %s%n",System.currentTimeMillis()/1000);
            }
        },1,5, TimeUnit.SECONDS);
    }
}
