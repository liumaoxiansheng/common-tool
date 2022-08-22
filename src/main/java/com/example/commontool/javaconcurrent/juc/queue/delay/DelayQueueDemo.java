package com.example.commontool.javaconcurrent.juc.queue.delay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueDemo {

    public static void main(String[] args) throws InterruptedException {

        //实例化一个DelayQueue
        BlockingQueue<DelayObject> blockingQueue = new DelayQueue<>();

        //向DelayQueue添加四个元素对象，注意延时时间不同
        blockingQueue.put(new DelayObject("A", 1000 * 10));  //延时10秒
        blockingQueue.put(new DelayObject("B", 4000 * 10));  //延时40秒
        blockingQueue.put(new DelayObject("C", 3000 * 10));  //延时30秒
        blockingQueue.put(new DelayObject("D", 2000 * 10));  //延时20秒

        //将对象从DelayQueue取出，注意取出的顺序与延时时间有关
        System.out.println(blockingQueue.take());  //取出A
        System.out.println(blockingQueue.take());  //取出D
        System.out.println(blockingQueue.take());  //取出C
        System.out.println(blockingQueue.take());  //取出B

    }

}


class DelayObject implements Delayed {
    private String name;
    private long time;   //延时时间

    public DelayObject(String name, long delayTime) {
        this.name = name;
        this.time = System.currentTimeMillis() + delayTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = time - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed obj) {
        if (this.time < ((DelayObject) obj).time) {
            return -1;
        }
        if (this.time > ((DelayObject) obj).time) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return "\nDelayObject:{"
                + "name=" + name
                + ", time=" + sd.format(date)
                + "}";
    }
}

