package com.example.commontool.designpatterns.pipleline.test;

import com.example.commontool.designpatterns.pipleline.BusiHandler;

import java.util.List;

/**
 * @ClassName FirstHandler
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/18
 **/
public class FirstHandler implements BusiHandler<List<Object>> {



    @Override
    public void handle(List<Object> data) {
        data.add(new Object());
        System.out.println("data"+data);
        System.out.println("FirstHandler.handle()......");
    }
}
