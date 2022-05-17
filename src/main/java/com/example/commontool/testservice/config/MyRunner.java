package com.example.commontool.testservice.config;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.example.commontool.utils.spring.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import sun.java2d.pipe.SpanIterator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @ClassName MyRunner
 * @Description redis 统计排名
 * @Author tianhuan
 * @Date 2022/4/7
 **/
//@Component
public class MyRunner implements ApplicationRunner {

    String articleHour="zset_user_like_article_rank:%s";

    String userLike="hash_user_like:%s";

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 1、获取最新：hash:dd-hh 遍历 field zincrby hour  + value
        //2、获取最近：X小时外的hash:dd-hh 遍历field  zincrby hour - value
        //   记录当前数据hincrby last：hash:_dd-hh artical-id count
        //   移除hash:dd-hh
        System.out.println("myRunner....run......");
        new Thread(()->{
            while (true){
                try {
                    countHourRank();
                    countDayRank();
                    Thread.sleep(1000*60*30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void countDayRank() {
        System.out.println("countDayRank....run......");
        String cache = LocalDateTimeUtil.format(LocalDateTime.now().plusDays(-1), "yyyy-MM-dd");
        LocalTime min = LocalTime.MIN;
        String cacheDayKey = String.format(userLike, cache);
        String dayRankKey = String.format(articleHour, "day");
        boolean needDay=false;
        if (!redisUtil.hasKey(cacheDayKey)) {
            needDay=true;
        }
        for (int i = 0; i < 24; i++) {
            LocalTime localTime = min.plusHours(i);
            String hh = localTime.format(DateTimeFormatter.ofPattern("-HH"));
            String perHourkey=String.format(userLike, cache)+hh;
            Map<Object, Object> hmget = redisUtil.hmget(perHourkey);
            if (hmget!=null) {
                for (Map.Entry<Object, Object> entry : hmget.entrySet()) {
                    String articleId = (String) entry.getKey();
                    Integer count = (Integer)entry.getValue();
                    redisUtil.zincr(dayRankKey,articleId,count);
                    if (needDay){
                        redisUtil.hincr(cacheDayKey,articleId,count);
                    }
                }
            }
        }

        String cacheOld = LocalDateTimeUtil.format(LocalDateTime.now().plusDays(-2), "yyyy-MM-dd");
        String cacheOldKey = String.format(userLike, cacheOld);
        Map<Object, Object> oldData = redisUtil.hmget(cacheOldKey);
        if (oldData!=null) {
            for (Map.Entry<Object, Object> entry : oldData.entrySet()) {
                String articleId = (String) entry.getKey();
                Integer count = 0-(Integer)entry.getValue();
                redisUtil.zincr(dayRankKey,articleId,count);
            }
        }
    }

    private void countHourRank() {
        System.out.println("countHourRank....run......");
        String cache = LocalDateTimeUtil.format(LocalDateTime.now().plusHours(-1), "yyyy-MM-dd-HH");
        String cacheKey = String.format(userLike, cache);
        Map<Object, Object> hmget = redisUtil.hmget(cacheKey);
        String articleHourKey = String.format(articleHour, "hour");
        if (hmget!=null) {
            for (Map.Entry<Object, Object> entry : hmget.entrySet()) {
                String articleId = (String) entry.getKey();
                Integer count = (Integer)entry.getValue();
                redisUtil.zincr(articleHourKey,articleId,count);
            }
        }

        String cacheOld = LocalDateTimeUtil.format(LocalDateTime.now().plusHours(-2), "yyyy-MM-dd-HH");
        String cacheOldKey = String.format(userLike, cacheOld);
        Map<Object, Object> oldData = redisUtil.hmget(cacheOldKey);
        if (oldData!=null) {
            for (Map.Entry<Object, Object> entry : oldData.entrySet()) {
                String articleId = (String) entry.getKey();
                Integer count = 0-(Integer)entry.getValue();
                redisUtil.zincr(articleHourKey,articleId,count);
            }
        }

    }
}
