package com.example.commontool.designpatterns.pipleline.test;

import com.example.commontool.designpatterns.pipleline.DefaultBusiPipeline;
import com.example.commontool.designpatterns.pipleline.ExecutorsBusiHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TestPipe
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/18
 **/
public class TestPipe {
    public static void main(String[] args) {
        DefaultBusiPipeline<List<Object>> pipeline = new DefaultBusiPipeline(new ArrayList<>());

        pipeline.addLast(new ExecutorsBusiHandlerContext("步骤1",new FirstHandler(),pipeline))
                .addLast(new ExecutorsBusiHandlerContext("步骤2",new SecondHandler(),pipeline))
                .addLast(new ExecutorsBusiHandlerContext("步骤3",new ThirdHandler(),pipeline));
//        pipeline.addLast("步骤1",new FirstHandler())
//                .addLast("步骤2",new SecondHandler())
//                .addLast("步骤3",new ThirdHandler());
        pipeline.start();
        List<Object> data = pipeline.getData();
        System.out.println("data = " + data);

    }
}
