package com.example.commontool.designpatterns.pipleline;

/**
 * @ClassName DefaultBusiHandlerContext
 * @Description 默认的上下文环境
 * @Author tianhuan
 * @Date 2022/5/18
 **/
public class DefaultBusiHandlerContext extends AbstractBusiHandlerContext{
    public DefaultBusiHandlerContext(String name, BusiHandler handler, AbstractBusiPipeline pipeline) {
        super(name, handler, pipeline);
    }
}
