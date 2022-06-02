package com.example.commontool.javaconcurrent.juc;

/**
 * @ClassName ThreadPriorityTest
 * @Description 线程优先测试
 * @Author tianhuan
 * @Date 2022/6/1
 **/
public class ThreadPriorityTest {

    public static void main(String[] args) {
        SellTicketTask ticketTask = new SellTicketTask(1000);
        Thread t1 = new Thread(ticketTask, "线程1");
        Thread t2 = new Thread(ticketTask, "线程2");
        Thread t3 = new Thread(ticketTask, "线程3");
        Thread t4 = new Thread(ticketTask, "线程4");
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        t3.setPriority(Thread.MAX_PRIORITY);
        t4.setPriority(Thread.MIN_PRIORITY);
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // 优先级并不是很靠谱，因为Java线程是通过映射到系统的原生线程上来实现的，所以线程调度最终还是取决于操作系统

    }

    public static class SellTicketTask implements Runnable {

        private ThreadLocal<Integer> tCount = new ThreadLocal<>();

        private int ticket;

        public SellTicketTask(int ticket) {
            this.ticket = ticket;
        }

        @Override
        public void run() {
            while (ticket > 0) {
                synchronized (this) {
                    if (ticket > 0) {
                        try {
                            // 线程进入暂时的休眠
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 获取到当前正在执行的程序的名称，打印余票
                        System.out.println(Thread.currentThread().getName() + ":正在执行操作，余票:" + ticket--);
                        if (tCount.get()==null) {
                            tCount.set(1);
                        }else {
                            tCount.set(tCount.get()+1);
                        }
                        System.out.println(Thread.currentThread().getName() + ":正在执行操作，抢票数:" +tCount.get());
                    }
                }
                //让出CPU
                Thread.yield();
            }
        }
    }
}
