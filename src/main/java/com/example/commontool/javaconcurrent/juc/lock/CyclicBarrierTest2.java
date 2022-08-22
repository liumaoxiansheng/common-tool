package com.example.commontool.javaconcurrent.juc.lock;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName CyclicBarrierTest2
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/6/10
 **/
public class CyclicBarrierTest2 {
    static ExecutorService runners = Executors.newFixedThreadPool(5);
    static List<Player> players = new ArrayList<>(5);

    static CountDownLatch prepare=new CountDownLatch(1);
    static CountDownLatch running=new CountDownLatch(1);

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(5, new Referee());
        // 5个运动准备，枪声响后一起再跑到终点，都到后记录排名
        System.out.println("比赛开始,运动员准备中...");

        for (int i = 0; i < 5; i++) {
            players.add(new Player("远动员" + (i + 1), barrier));
        }
        for (Player player : players) {
            runners.execute(() -> {
                player.prepare();
            });
        }
        try {
            prepare.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("运动员飞奔中...");
        for (Player player : players) {
            runners.execute(() -> {
                player.running();
            });
        }
        try {
            running.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("比赛结束...计算排名中....");


        runners.shutdown();
    }

    public static class Referee implements Runnable {

        @Override
        public void run() {
            boolean isPrepare = true;
            boolean isFinished = true;
            for (Player player : players) {
                isPrepare = player.isPrepare() && isPrepare;
                isFinished = player.isFinished() && isFinished;
            }
            if (isPrepare && !isFinished) {
                prepare.countDown();
            }
            if (isFinished) {
                running.countDown();
            }
        }
    }

    public static class Player {

        private CyclicBarrier barrier;

        private String name;

        private long start;
        private long end;

        private boolean isPrepare = false;
        private boolean isFinished = false;

        public boolean isPrepare() {
            return isPrepare;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public Player(String name, CyclicBarrier barrier) {
            this.name = name;
            this.barrier = barrier;
        }


        public void prepare() {
            int i = RandomUtil.randomInt(100);
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                System.out.println(name + "准备完毕...");
                isPrepare = true;
                barrier.await();
                start = System.currentTimeMillis();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }

        public void running() {
            int i = RandomUtil.randomInt(100);
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                System.out.println(name + "到达终点...");
                end = System.currentTimeMillis();
                isFinished = true;
                barrier.await();
                System.out.println(name + "花费时间" + (end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }

    }
}
