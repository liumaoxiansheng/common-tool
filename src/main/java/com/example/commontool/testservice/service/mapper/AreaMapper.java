package com.example.commontool.testservice.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.commontool.testservice.model.AreaDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName: AreaMapper
 * @Description:
 * @Author: th_legend
 **/
@Mapper
public interface AreaMapper extends BaseMapper<AreaDO> {

    @Select("select * from ")
    public String abd();
}
