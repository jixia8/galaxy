package com.example.galaxy.security.filter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptionTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456"; // 明文密码
        String encodedPassword = encoder.encode(rawPassword); // 加密密码
        System.out.println("加密后的密吗：" + encodedPassword);
    }
}