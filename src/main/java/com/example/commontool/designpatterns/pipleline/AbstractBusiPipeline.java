package com.example.commontool.designpatterns.pipleline;

/**
 * @ClassName AbstractBusiPipeline
 * @Description pipeline处理
 * @Author tianhuan
 * @Date 2022/5/18
 **/
public abstract class AbstractBusiPipeline<T> implements BusiPipeline<T> {

    AbstractBusiHandlerContext tail;

    final T data;

    AbstractBusiHandlerContext head;

    public AbstractBusiPipeline(T data) {
        this.tail = TailContext.tail(this);
        this.head = HeadContext.head(this);
        this.data = data;
    }

    @Override
    public BusiPipeline<T> addFirst(String var1, BusiHandler<T> var2) {
        DefaultBusiHandlerContext context = new DefaultBusiHandlerContext(var1, var2, this);
        context.pre = this.head;
        AbstractBusiHandlerContext next = (AbstractBusiHandlerContext)this.head.next;
        next.pre=context;
        context.next=next;
        this.head.next = context;
        return this;
    }

    @Override
    public BusiPipeline<T> addFirst(BusiHandlerContext ctx) {
        AbstractBusiHandlerContext context=(AbstractBusiHandlerContext)ctx;
        context.pre = this.head;
        AbstractBusiHandlerContext next = (AbstractBusiHandlerContext)this.head.next;
        next.pre=context;
        context.next=next;
        this.head.next = context;
        return this;
    }

    @Override
    public BusiPipeline<T> addLast(String var1, BusiHandler<T> var2) {
        DefaultBusiHandlerContext context = new DefaultBusiHandlerContext(var1, var2, this);
        context.next = this.tail;
        AbstractBusiHandlerContext pre = (AbstractBusiHandlerContext)this.tail.pre;
        pre.next=context;
        context.pre=pre;
        this.tail.pre = context;
        return this;
    }
    @Override
    public BusiPipeline<T> addLast(BusiHandlerContext ctx) {
        AbstractBusiHandlerContext context=(AbstractBusiHandlerContext)ctx;
        context.next = this.tail;
        AbstractBusiHandlerContext pre = (AbstractBusiHandlerContext)this.tail.pre;
        pre.next=context;
        context.pre=pre;
        this.tail.pre = context;
        return this;
    }

    @Override
    public  T getData(){
        return this.data;
    }

    private final static class TailContext extends AbstractBusiHandlerContext {

        public TailContext(String name, BusiHandler handler, AbstractBusiPipeline pipeline) {
            super(name, handler, pipeline,false,true);
        }

        public TailContext(AbstractBusiPipeline pipeline) {
            this("##TailContext##", new BusiHandler() {
                @Override
                public void handle(Object data) {
                    System.out.println("业务执行到尾部.....");
                }
            }, pipeline);
        }

        public static TailContext tail(AbstractBusiPipeline pipeline) {
            TailContext tailContext = new TailContext(pipeline);
            if (pipeline.head != null) {
                pipeline.head.next = tailContext;
                tailContext.pre = pipeline.head;
                tailContext.next=pipeline.head;
                pipeline.head.pre=tailContext;
            }
            return tailContext;
        }
    }

    private final static class HeadContext extends AbstractBusiHandlerContext {

        public HeadContext(String name, BusiHandler handler, AbstractBusiPipeline pipeline) {
            super(name, handler, pipeline,true,false);
        }

        public HeadContext(AbstractBusiPipeline pipeline) {
            this("##HeadContext##", new BusiHandler() {
                @Override
                public void handle(Object data) {
                    System.out.println("业务执行到头部.....");
                }
            }, pipeline);
        }

        public static HeadContext head(AbstractBusiPipeline pipeline) {
            HeadContext headContext = new HeadContext(pipeline);
            if (pipeline.tail != null) {
                pipeline.tail.pre = headContext;
                headContext.next = pipeline.tail;
                headContext.pre=pipeline.tail;
                pipeline.tail.next=headContext;
            }
            return headContext;
        }
    }
}
