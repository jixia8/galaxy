package com.example.galaxy.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private Integer code;
    private String message;
    private T data;

    public R(int i, String 成功, T data) {
    }

    public static <T> R<T> success(T data) {
        return new R<>(200, "成功", data);
    }

    public static <T> R<T> failed(String message) {
        return new R<>(500, message, null);
    }

    // 构造函数省略
}