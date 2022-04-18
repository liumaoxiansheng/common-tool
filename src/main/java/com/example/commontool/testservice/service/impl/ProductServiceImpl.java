package com.example.commontool.testservice.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName ProductImplSercive
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/4/15
 **/
@Service
public class ProductServiceImpl {
    @Autowired
    private RedissonClient redissonClient;

    private String PRO_CACHE_PREFIX = "product_cache:";
    private String LOCK_HOT_PRO_GET_PREFIX = "lock:hot_product_get:";
    private String LOCK_HOT_PRO_CREATE_PREFIX = "lock:hot_product_create:";
    private String PRO_EMPTY = "{}";
    private long EMPTY_PRO_TTL_SECOND = 3 * 60;
    private long HOT_PRO_TTL_SECOND = 10 * 60;

    private long ttl(long second) {
        long randomVal = second / 3;
        long addSecond = RandomUtil.randomLong(randomVal);
        return second + addSecond;
    }

    @Data
    public static class Product {
        private String id;
        private String name;
    }

    public void add(Product product) {
        // 更新是加写锁
        String productKey = PRO_CACHE_PREFIX + product.getId();
        String productCreateLockKey = LOCK_HOT_PRO_CREATE_PREFIX + product.getId();
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(productCreateLockKey);
        readWriteLock.writeLock().lock();
        try {
            saveToDB(product);
            RBucket<Object> bucket = redissonClient.getBucket(productKey);
            bucket.set(JSONUtil.toJsonStr(product));
            bucket.expire(ttl(HOT_PRO_TTL_SECOND), TimeUnit.MINUTES);
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

    private void saveToDB(Product product) {

    }

    public void update(Product product) {

    }

    public Product get(String id) {
        Product product = null;
        // 先从缓存
        String productKey = PRO_CACHE_PREFIX + id;
        product = getProductFromCache(productKey);
        if (product != null) {
            return product;
        }
        // 突发性冷门数据变成热门数据,double check
        String hotProductLockKey = LOCK_HOT_PRO_GET_PREFIX + id;
        RLock hotProductLock = redissonClient.getLock(hotProductLockKey);
        hotProductLock.lock();
//        try {
//            hotProductLock.tryLock(3,TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            // 防止突发性冷门数据变成热门数据,double check导致缓存击穿
            product = getProductFromCache(productKey);
            if (product != null) {
                return product;
            }
            // 双写缓存数据一致性问题处理 读多写少，读写锁
            String productCreateLockKey = LOCK_HOT_PRO_CREATE_PREFIX + id;
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(productCreateLockKey);
            readWriteLock.readLock().lock();
            try {
                product = getProductFromDB(id);
                RBucket<Object> bucket = redissonClient.getBucket(productKey);
                if (product == null) {
                    bucket.set(PRO_EMPTY);
                    bucket.expire(EMPTY_PRO_TTL_SECOND, TimeUnit.SECONDS);
                    return new Product();
                } else {
                    bucket.set(JSONUtil.toJsonStr(product));
                    bucket.expire(ttl(HOT_PRO_TTL_SECOND), TimeUnit.MINUTES);
                    return product;
                }
            } finally {
                readWriteLock.readLock().unlock();
            }
        } finally {
            hotProductLock.unlock();
        }
    }

    public Product getProductFromCache(String productKey) {
        Product product = null;
        // 先从缓存
        RBucket<Object> rBucket = redissonClient.getBucket(productKey);
        String cacheString = (String) rBucket.get();
        if (cacheString == null) {
            return null;
        }
        if (PRO_EMPTY.equals(cacheString)) {
            rBucket.expire(EMPTY_PRO_TTL_SECOND, TimeUnit.SECONDS);
            return new Product();
        }
        product = JSONUtil.toBean(cacheString, Product.class);
        rBucket.expire(ttl(HOT_PRO_TTL_SECOND), TimeUnit.MINUTES);
        return product;
    }

    private Product getProductFromDB(String productId) {
        Product product = new Product();
        product.setId(productId);
        return product;
    }
}
