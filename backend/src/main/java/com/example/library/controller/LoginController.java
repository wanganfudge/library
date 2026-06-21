package com.example.library.controller;

import com.example.library.entity.LoginRequest;
import com.example.library.entity.LoginResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    // 固定账号密码
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "123456";

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        if (ADMIN_USER.equals(request.getUsername()) &&
                ADMIN_PASS.equals(request.getPassword())) {
            return new LoginResponse(true, "登录成功", "admin-token-123");
        }
        return new LoginResponse(false, "用户名或密码错误", null);
    }
}