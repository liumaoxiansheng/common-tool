package com.example.commontool.testservice.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName: AreaDO
 * @Description: TODO 类描述
 * @Author: th_legend
 * @Date: 2022/1/21
 **/
@Data
@TableName(value = "area")
@Accessors(chain = true)
public class AreaDO implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)//指定自增策略
    private Integer id;
    private String name;
    private String location;
}
