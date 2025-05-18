package com.example.galaxy.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStructure<T> {
    private int code;
    private String message;
    private T data;

    // 静态工厂方法
    public static <T> ResponseStructure<T> success(T data) {
        return new ResponseStructure<>(200, "成功", data);
    }

    public static <T> ResponseStructure<T> failed(String message) {
        return new ResponseStructure<>(500, message, null);
    }

    public static <T> ResponseStructure<T> instance(int code, String message) {
        return new ResponseStructure<>(code, message, null);
    }
}