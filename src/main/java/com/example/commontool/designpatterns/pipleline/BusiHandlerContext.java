package com.example.commontool.designpatterns.pipleline;

/**
 * @ClassName BusiHandlerContext
 * @Description 业务执行上下文，控制业务执行器的程序流转
 * @Author tianhuan
 * @Date 2022/5/18
 **/
public interface BusiHandlerContext {

    /**
     * 返回业务的handler
     *
     * @return com.example.commontool.designpatterns.pipleline.BusiHandler
     * @Author tianhuan
     * @Date 2022/5/18
     **/
    BusiHandler handler();

    /**
     *
     *往后传递执行
     * @return com.example.commontool.designpatterns.pipleline.BusiHandlerContext
     * @Author tianhuan
     * @Date 2022/5/18
     **/
    BusiHandlerContext nextHandle();

    /**
     *
     *往前传递执行
     * @return com.example.commontool.designpatterns.pipleline.BusiHandlerContext
     * @Author tianhuan
     * @Date 2022/5/18
     **/
    BusiHandlerContext preHandle();
}
