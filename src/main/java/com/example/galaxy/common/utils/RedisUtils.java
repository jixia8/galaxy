package com.example.galaxy.common.utils;



public interface RedisUtils {
    Object get(String key);
    boolean set(String key, Object value, long time);
    boolean sSetAndTime(String key, long time, Object... values);
    boolean delete(String key); // 添加删除方法
}
