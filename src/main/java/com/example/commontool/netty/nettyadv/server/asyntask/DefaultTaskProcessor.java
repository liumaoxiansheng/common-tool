package com.example.commontool.netty.nettyadv.server.asyntask;

import com.example.commontool.netty.nettyadv.vo.MyMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName DefaultTaskProcessor
 * @Description 任务线程
 * @Author tianhuan
 * @Date 2022/5/19
 **/
@Slf4j
public class DefaultTaskProcessor implements ITaskProcessor{
    @Override
    public Runnable execAsyncTask(MyMessage msg) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.info("DefaultTaskProcessor模拟任务处理："+msg.getBody());
            }
        };
        return task;
    }
}
