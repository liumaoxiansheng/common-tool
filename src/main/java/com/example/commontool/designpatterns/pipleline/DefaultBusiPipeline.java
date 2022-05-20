package com.example.commontool.designpatterns.pipleline;

/**
 * @ClassName DefaultBusiPipeline
 * @Description 默认的pipeline
 * @Author tianhuan
 * @Date 2022/5/18
 **/
public class DefaultBusiPipeline<T> extends AbstractBusiPipeline<T>{


    public DefaultBusiPipeline(T data) {
        super(data);
    }

    public void start(){
        AbstractBusiHandlerContext ctx=this.head;
        while (ctx.next!=null && !ctx.isTail){
             ctx.nextHandle();
             ctx=(AbstractBusiHandlerContext)ctx.next;
        }
    }

    public void startRevers(){
        AbstractBusiHandlerContext ctx=this.tail;
        while (ctx.pre!=null && !ctx.isHead){
             ctx.preHandle();
             ctx=(AbstractBusiHandlerContext)ctx.pre;
        }
    }

    public void startCycle(){
        AbstractBusiHandlerContext ctx=this.head;
        while (true){
            ctx.nextHandle();
            ctx=(AbstractBusiHandlerContext)ctx.next;
        }
    }

    public void startCycleRevers(){
        AbstractBusiHandlerContext ctx=this.tail;
        while (true){
            ctx.nextHandle();
            ctx=(AbstractBusiHandlerContext)ctx.pre;
        }
    }

}
