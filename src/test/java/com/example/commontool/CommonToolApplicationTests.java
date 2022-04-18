package com.example.commontool;

import com.example.commontool.testservice.model.User;
import com.example.commontool.testservice.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommonToolApplicationTests {

    @Autowired
    private IUserService userService;

    @Test
    void contextLoads() {
       // userService.addUser(new User().setName("李10").setSex("女"));
        userService.test();
    }



}
