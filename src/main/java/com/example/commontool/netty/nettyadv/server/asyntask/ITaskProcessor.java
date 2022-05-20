package com.example.commontool.netty.nettyadv.server.asyntask;

import com.example.commontool.netty.nettyadv.vo.MyMessage;

/**
 * @ClassName ITaskProcessor
 * @Description 异步任务处理器
 * @Author tianhuan
 * @Date 2022/5/19
 **/
public interface ITaskProcessor {

    /**
     * 执行异步任务
     *
     * @param msg:
     * @return java.lang.Runnable
     * @Author tianhuan
     * @Date 2022/5/19
     **/
    Runnable execAsyncTask(MyMessage msg);
}
