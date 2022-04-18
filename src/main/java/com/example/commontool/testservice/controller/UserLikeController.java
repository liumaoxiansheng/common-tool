package com.example.commontool.testservice.controller;

import com.example.commontool.testservice.service.IUserLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserLikeController
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/4/7
 **/
@RestController
@RequestMapping("/userLike")
public class UserLikeController {

    @Autowired
    private IUserLikeService userLikeService;

    @GetMapping("/like")
    public String like(@RequestParam Long articleId){
        userLikeService.like(articleId);
        return "ok";
    }


    @GetMapping("/deduct")
    public String deduct(){
       //
        return userLikeService.deduct();
    }
}
