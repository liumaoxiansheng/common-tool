package com.example.commontool.designpatterns.pipleline;

/**
 * @ClassName BusiPipeline
 * @Description pipeline管理上下文
 * @Author tianhuan
 * @Date 2022/5/18
 **/
public interface BusiPipeline<T> {

    /**
     * 添加在首部
     *
     * @param var1: 名字
     * @param var2: 业务执行器
     * @return com.example.commontool.designpatterns.pipleline.BusiPipeline
     * @Author tianhuan
     * @Date 2022/5/18
     **/
    BusiPipeline<T> addFirst(String var1, BusiHandler<T> var2);

    /**
     * 添加在首部
     *
     * @param context: 业务执行器
     * @return com.example.commontool.designpatterns.pipleline.BusiPipeline
     * @Author tianhuan
     * @Date 2022/5/18
     **/
    BusiPipeline<T> addFirst(BusiHandlerContext context);


    /**
     * 添加在尾部
     *
     * @param var1: 名字
     * @param var2: 业务执行器
     * @return com.example.commontool.designpatterns.pipleline.BusiPipeline
     * @Author tianhuan
     * @Date 2022/5/18
     **/
    BusiPipeline<T> addLast(String var1, BusiHandler<T> var2);

    /**
     * 添加在尾部
     *
     * @param context: 上下文
     * @return com.example.commontool.designpatterns.pipleline.BusiPipeline
     * @Author tianhuan
     * @Date 2022/5/18
     **/
    BusiPipeline<T> addLast(BusiHandlerContext context);

      T getData();



}
