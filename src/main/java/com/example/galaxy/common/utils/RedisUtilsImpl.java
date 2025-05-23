package com.example.galaxy.common.utils;

import com.sun.org.slf4j.internal.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
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
        try {
            SetOperations<String, Object> setOps = redisTemplate.opsForSet();
            setOps.add(key, values);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
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
}