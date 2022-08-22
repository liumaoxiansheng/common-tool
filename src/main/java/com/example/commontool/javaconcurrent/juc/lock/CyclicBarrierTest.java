package com.example.commontool.javaconcurrent.juc.lock;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName CyclicBarrierTest
 * @Description 循环阑珊测试
 * @Author tianhuan
 * @Date 2022/6/10
 **/
public class CyclicBarrierTest {

    static ExecutorService runners = Executors.newFixedThreadPool(5);
    static List<Player> players = new ArrayList<>(5);

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(5,()->{
            System.out.println("完毕！！！");
        });
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
        for (Player player : players) {
            runners.execute(() -> {
                player.running();
            });
        }

        runners.shutdown();
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
                start=System.currentTimeMillis();
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
                end=System.currentTimeMillis();
                isFinished = true;
                barrier.await();
                System.out.println(name+"花费时间"+(end-start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }

    }
}
