package com.example.commontool.testservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.commontool.testservice.model.User;
import com.example.commontool.testservice.service.mapper.UserMapper;

/**
 * @ClassName: IUserService
 * @Description: TODO 类描述
 * @Author: th_legend
 * @Date: 2022/1/21
 **/
public interface IUserService extends IService<User> {

    void addUser(User user);

    void test();

}
