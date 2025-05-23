package com.example.galaxy.entity.DTO;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    private String userAvatarUrl; // 可选：注册时允许上传头像地址

    @NotBlank(message = "账号不能为空")
    private String userAccount;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度应在 6-20 位之间")
    private String userPassword;

    @Email(message = "邮箱格式不正确")
    private String userEmail;
}