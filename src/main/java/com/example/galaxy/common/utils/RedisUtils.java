package com.example.galaxy.common.utils;


import java.util.Map;

public interface RedisUtils {
    Object get(String key);
    boolean set(String key, Object value, long time);
    boolean sSetAndTime(String key, long time, Object... values);
    boolean delete(String key); // 添加删除方法

    Long hIncrement(String key, String field, long delta);

    Map<String, Object> getHash(String key);

    void expire(String key, long seconds);
}
