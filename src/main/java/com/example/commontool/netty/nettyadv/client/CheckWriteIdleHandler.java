package com.example.commontool.netty.nettyadv.client;

import io.netty.handler.timeout.IdleStateHandler;
import org.omg.PortableInterceptor.INACTIVE;

/**
 * @ClassName CheckWriteIdleHandler
 * @Description 检测写空闲
 * @Author tianhuan
 * @Date 2022/5/19
 **/
public class CheckWriteIdleHandler extends IdleStateHandler {
    public CheckWriteIdleHandler(int writeTimeOut) {
        super(0, writeTimeOut, 0);
    }
}
