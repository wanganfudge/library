package com.example.library.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有异常
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(Exception e) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "系统错误：" + e.getMessage());
        // 生产环境不要暴露详细错误，用：result.put("message", "系统繁忙，请稍后重试");
        return result;
    }

    // 捕获空指针
    @ExceptionHandler(NullPointerException.class)
    public Map<String, Object> handleNull(NullPointerException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "数据不能为空");
        return result;
    }
}