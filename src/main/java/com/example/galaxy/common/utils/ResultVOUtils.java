package com.example.galaxy.common.utils;

import com.example.galaxy.VO.ResultVO;

import javax.servlet.http.HttpServletResponse;

public class ResultVOUtils {

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(200, "操作成功", data);
    }

    public static <T> ResultVO<T> success(String message) {
        return new ResultVO<>(200, message, null);
    }
    public static <T> ResultVO<T> success() {
        return new ResultVO<>(200, "操作成功", null);
    }

    public static <T> ResultVO<T> success(String message, T data) {
        return new ResultVO<>(200, message, data);
    }

    public static <T> ResultVO<T> failed(String message) {
        return new ResultVO<>(500, message, null);
    }

    public static <T> ResultVO<T> failed(int code, String message) {
        return new ResultVO<>(code, message, null);
    }

}