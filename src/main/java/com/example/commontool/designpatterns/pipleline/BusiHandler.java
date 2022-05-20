package com.example.commontool.designpatterns.pipleline;

/**
 * @ClassName BusiHandler
 * @Description 业务执行器
 * @Author tianhuan
 * @Date 2022/5/18
 **/
public interface BusiHandler<T> {

    /**
     * 业务执行方法
     *
     * @param data: 传入的参数
     * @return void
     * @Author tianhuan
     * @Date 2022/5/18
     **/
    void handle(T data);
}
