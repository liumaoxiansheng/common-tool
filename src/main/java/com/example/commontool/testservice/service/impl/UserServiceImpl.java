package com.example.commontool.testservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.commontool.testservice.model.AreaDO;
import com.example.commontool.testservice.model.User;
import com.example.commontool.testservice.service.IAreaService;
import com.example.commontool.testservice.service.IUserService;
import com.example.commontool.testservice.service.mapper.UserMapper;
import com.example.commontool.utils.spring.transaction.MyTransactionOption;
import com.example.commontool.utils.spring.transaction.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @ClassName: UserServiceImpl
 * @Description: TODO 类描述
 * @Author: th_legend
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private IAreaService areaService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TransactionUtil transactionUtil;

    @Override
   // @Transactional(rollbackFor = Exception.class)
    public void addUser(User user) {
        mapper.insert(user);
//        areaService.addArea(new AreaDO().setName("深圳3").setLocation("01，02"));
//        TransactionStatus status=null;
//
//        try {
//            status = transactionUtil.begin();
//            TransactionSynchronizationManager.registerSynchronization(new MyTransactionOption());
//            addUser2(user);
//            ConnectionHolder resource = (ConnectionHolder) TransactionSynchronizationManager.getResource(transactionUtil.getDataSource());
//            System.out.println("addUser---TransactionSynchronizationManager.getCurrentTransactionName() = " + resource.getConnection());
//            transactionUtil.commit(status);
//        } catch (Exception e) {
//            transactionUtil.rollback(status);
//        }
    }

    @Override
    public void test() {
        User user = mapper.test("张三", 20);
        System.out.println("user = " + user);
    }


    // @Transactional(rollbackFor = Exception.class)
    public void addUser2(User user) {
        mapper.insert(user);
//        try {
//            areaService.addArea(new AreaDO().setName("深圳8").setLocation("16，17"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

      //   throw  new RuntimeException("异常2");
    }
}
