package com.example.commontool.testservice.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.example.commontool.testservice.service.IUserLikeService;
import com.example.commontool.utils.spring.RedisUtil;
import org.apache.poi.ss.formula.functions.T;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserLikeServiceImpl
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/4/7
 **/
@Service
public class UserLikeServiceImpl implements IUserLikeService {

    String userLike = "hash_user_like:%s";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

   // @Autowired
    private RedissonClient redissonClient;

    @Override
    public void like(Long articleId) {
        String cache = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd-HH");
        String cacheKey = String.format(userLike, cache);
        redisUtil.hincr(cacheKey, String.valueOf(articleId), 1);
    }

    @Override
    public String deduct() {
        String lockKey = "lock:pro-100";
        UUID uuid = UUID.randomUUID();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, uuid, 10, TimeUnit.SECONDS);
        if (!lock) {
            return "error_code";
        }
        // 锁续命
        new Thread(() -> {
            while (true) {
                if (uuid.equals(redisTemplate.opsForValue().get(lockKey))) {
                    redisTemplate.expire(lockKey, 10, TimeUnit.SECONDS);
                } else {
                    break;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            String productKey = "pro-100";
            Integer count = (Integer) redisTemplate.opsForValue().get(productKey);
            count--;
            redisTemplate.opsForValue().set(productKey, count);
            System.out.println("剩余库存：：" + count);
        } catch (Exception e) {
            System.out.println("系统异常");
        } finally {
            if (uuid.equals(redisTemplate.opsForValue().get(lockKey))) {
                redisTemplate.delete(lockKey);
            }
        }

        return "ok";
    }

    public String deduc2() {
        String lockKey = "lock:pro-100";
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        // 锁续命
        try {
            String productKey = "pro-100";
            Integer count = (Integer) redisTemplate.opsForValue().get(productKey);
            count--;
            redisTemplate.opsForValue().set(productKey, count);
            System.out.println("剩余库存：：" + count);
        } catch (Exception e) {
            System.out.println("系统异常");
        } finally {
            lock.unlock();
        }
        return "ok";
    }

    public String deduc3() {
        String lockKey = "lock:pro-100";
        UUID uuid = UUID.randomUUID();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, uuid, 30, TimeUnit.SECONDS);
        if (!lock) {
            return "error_code";
        }
        // 锁续命
        new Thread(()->{
            while (true){
                if (uuid.equals(redisTemplate.opsForValue().get(lockKey))) {
                    redisTemplate.expire(lockKey,30,TimeUnit.SECONDS);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    return;
                }
            }
        }).start();
        try {
            String productKey = "pro-100";
            Integer count = (Integer) redisTemplate.opsForValue().get(productKey);
            count--;
            redisTemplate.opsForValue().set(productKey, count);
            System.out.println("剩余库存：：" + count);
        } catch (Exception e) {
            System.out.println("系统异常");
        } finally {
            if (uuid.equals(redisTemplate.opsForValue().get(lockKey))){
                redisTemplate.delete(lockKey);
            }
        }

        return "ok";
    }
}
