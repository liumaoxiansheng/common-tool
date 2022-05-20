package com.example.commontool.designpatterns.pipleline;

/**
 * @ClassName AbstractBusiHandlerContext
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/18
 **/
public abstract class AbstractBusiHandlerContext implements BusiHandlerContext{

     BusiHandlerContext pre;

     BusiHandlerContext next;

   final   BusiHandler handler;

   final   AbstractBusiPipeline pipeline;

    final String name;

    final boolean isHead;
    final boolean isTail;

    public AbstractBusiHandlerContext(String name,BusiHandler handler ,AbstractBusiPipeline pipeline){
        this.name=name;
        this.handler=handler;
        this.pipeline=pipeline;
        this.pre=null;
        this.next=null;
        this.isHead=false;
        this.isTail=false;
    }
    public AbstractBusiHandlerContext(String name,BusiHandler handler ,AbstractBusiPipeline pipeline,boolean isHead,boolean isTail ){
        this.name=name;
        this.handler=handler;
        this.pipeline=pipeline;
        this.pre=null;
        this.next=null;
        this.isHead=isHead;
        this.isTail=isTail;
    }


    @Override
    public BusiHandler handler() {
        return handler;
    }

    @Override
    public BusiHandlerContext nextHandle() {
        this.next.handler().handle(pipeline.data);
        return this;
    }

    @Override
    public BusiHandlerContext preHandle() {
        this.pre.handler().handle(pipeline.data);
        return this;
    }
}
