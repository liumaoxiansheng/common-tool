package com.example.commontool.testservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.commontool.testservice.model.AreaDO;

/**
 * @ClassName: IAreaService
 * @Description:
 * @Author: th_legend
 **/
public interface IAreaService extends IService<AreaDO> {
    void addArea(AreaDO area);
}
