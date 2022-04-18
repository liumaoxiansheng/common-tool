package com.example.commontool.testservice.service;

/**
 * @ClassName IUserLikeService
 * @Description 用户点赞服务
 * @Author tianhuan
 * @Date 2022/4/7
 **/
public interface IUserLikeService {
    void like(Long articleId);

    String deduct();

}
