package com.example.commontool.testservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.commontool.testservice.model.AreaDO;
import com.example.commontool.testservice.service.IAreaService;
import com.example.commontool.testservice.service.mapper.AreaMapper;
import com.example.commontool.utils.spring.transaction.MyTransactionOption;
import com.example.commontool.utils.spring.transaction.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @ClassName: AreaServiceImpl
 * @Description: TODO 类描述
 * @Author: th_legend
 **/
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, AreaDO> implements IAreaService {

    @Autowired
    private TransactionUtil transactionUtil;

    @Override
    //@Transactional(rollbackFor = Exception.class,propagation = Propagation.NESTED)
    public void addArea(AreaDO area) {
        TransactionStatus status=null;
        try {
            status = transactionUtil.begin();
            TransactionSynchronizationManager.registerSynchronization(new MyTransactionOption());
            this.baseMapper.insert(area);
            ConnectionHolder resource = (ConnectionHolder) TransactionSynchronizationManager.getResource(transactionUtil.getDataSource());
            System.out.println("addArea---TransactionSynchronizationManager.getCurrentTransactionName() = " + resource.getConnection());
            if (1==1){
                throw  new RuntimeException("异常");
            }
            transactionUtil.commit(status);
        } catch (Exception e) {
            transactionUtil.rollback(status);
        }

    }
}
