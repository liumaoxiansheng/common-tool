package com.example.commontool.testservice.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.commontool.testservice.DynamicInjectArgs;
import com.example.commontool.testservice.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.AggregateFunction;

/**
 * @ClassName: UserMapper
 * @Description:
 * @Author: th_legend
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @DynamicInjectArgs({"name","age2"})
    User test(@Param("name") String name, @Param("age")Integer age);
}
