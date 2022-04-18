package com.example.commontool.testservice;

import java.lang.annotation.*;

/**
 * @ClassName: DynamicInjectArgs
 * @Description: TODO 类描述
 * @Author: th_legend
 * @Date: 2022/2/25
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicInjectArgs {
    String[] value() default {};
}
