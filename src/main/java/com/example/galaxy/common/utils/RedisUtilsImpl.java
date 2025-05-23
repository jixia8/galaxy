package com.example.galaxy.common.utils;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtilsImpl implements RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 普通缓存获取
     */
    @Override
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入并设置时间（秒）
     */
    @Override
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Set集合添加并设置时间（秒）
     */
    @Override
    public boolean sSetAndTime(String key, long time, Object... values) {
        if (!StringUtils.hasText(key) || values == null || values.length == 0) {
            return false;
        }
        Logger logger = LoggerFactory.getLogger(RedisUtilsImpl.class);
        try {
            SetOperations<String, Object> setOps = redisTemplate.opsForSet();
            setOps.add(key, values);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            logger.error("Redis Set add with expire failed. key: {}, values: {}", key, values, e);
            return false;
        }
    }
    /**
     * 删除缓存
     */
    @Override
    public boolean delete(String key) {
        if (!StringUtils.hasText(key)) {
            return false; // 如果 key 为空或仅包含空白字符，直接返回 false
        }
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            // 使用日志记录异常
            LoggerFactory.getLogger(RedisUtilsImpl.class).error("Failed to delete key: {}", key, e);
            return false;
        }
    }
    @Override
    public Long hIncrement(String key, String field, long delta) {
        return redisTemplate.opsForHash().increment(key, field, delta);
    }
    @Override
    public Map<String, Object> getHash(String key) {
        Map<Object, Object> rawMap = redisTemplate.opsForHash().entries(key);
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : rawMap.entrySet()) {
            result.put(entry.getKey().toString(), entry.getValue());
        }
        return result;
    }

    @Override
    public void expire(String key, long seconds) {
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }
}