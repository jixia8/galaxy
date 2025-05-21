package com.example.galaxy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor

public class SysUser {
    private long userId;
    private String userName;
    private String userAvatarUrl;
    @NotBlank(message = "账号不能为空")
    private String userAccount;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度在6-20之间")
    private String userPassword;
    private String userEmail;
    private Boolean accountNonExpired;	//账号是否未过期（true 表示有效）
    private Boolean accountNonLocked;	//账号是否未锁定（true 表示未被锁）
    private Boolean credentialsNonExpired;	//凭证是否未过期（true 表示密码有效）
    private Boolean enabled; //可用性
    public SysUser(SysUserDTO sysUserDTO) {
        this.userId = sysUserDTO.getUserId();
        this.userName = sysUserDTO.getUserName();
        this.userAvatarUrl = sysUserDTO.getUserAvatarUrl();
        this.userAccount = sysUserDTO.getUserAccount();
        this.userEmail = sysUserDTO.getUserEmail();
    }
//    private List<SysRole> userToRoles;
}
