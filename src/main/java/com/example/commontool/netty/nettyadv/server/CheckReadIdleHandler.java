package com.example.commontool.netty.nettyadv.server;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @ClassName CheckReadIdleHandler
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/5/20
 **/
public class CheckReadIdleHandler extends IdleStateHandler {
    public CheckReadIdleHandler(int readerIdleTimeSeconds) {
        super(readerIdleTimeSeconds, 0, 0);
    }
}
