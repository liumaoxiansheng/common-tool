package com.example.commontool.designpatterns.pipleline;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName ExecutorsBusiHandlerContext
 * @Description 线程池
 * @Author tianhuan
 * @Date 2022/5/19
 **/
public class ExecutorsBusiHandlerContext extends AbstractBusiHandlerContext {
    public ExecutorsBusiHandlerContext(String name, BusiHandler handler, AbstractBusiPipeline pipeline, ExecutorService executors) {
        super(name, handler, pipeline);
        this.executors = getExecutors(executors);
    }
    public ExecutorsBusiHandlerContext(String name, BusiHandler handler, AbstractBusiPipeline pipeline) {
        this(name,handler,pipeline,null);
    }

    final private ExecutorService executors;

    private ExecutorService getExecutors(ExecutorService executors) {
        if (executors == null) {
            executors = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(), new ExecutorsBusiThreadFactory(this.name));
        }
        return executors;
    }

    @Override
    public BusiHandlerContext nextHandle() {
        List<Future> futures=new LinkedList<>();
        Future<?> future = executors.submit(() -> {
            this.next.handler().handle(pipeline.data);
            return 1;
        });
        futures.add(future);
        try {
            for (Future f : futures) {
                f.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executors.shutdown();
        }
        return this;
    }

    @Override
    public BusiHandlerContext preHandle() {
        List<Future> futures=new LinkedList<>();
        Future<Integer> future = executors.submit(() -> {
            this.pre.handler().handle(pipeline.data);
            return 1;
        });
        futures.add(future);
        try {
            for (Future f : futures) {
                f.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executors.shutdown();
        }
        return this;
    }

    static class ExecutorsBusiThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        ExecutorsBusiThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "-ExecutorsBusiHandlerContext-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        ExecutorsBusiThreadFactory(String namePrefixAdd) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "-ExecutorsBusiHandlerContext-" +
                    poolNumber.getAndIncrement() + "-" + namePrefixAdd +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
