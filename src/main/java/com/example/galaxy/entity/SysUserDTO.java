package com.example.galaxy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
/*
  SysUserDTO是一个数据传输对象，用于在系统中传递用户信息
  该类包含了用户的基本信息，如用户ID、用户名、头像URL、账号和邮箱等
  该类的构造函数接收一个SysUser对象，并将其属性赋值给DTO对象
 */
public class SysUserDTO {
    private long userId;
    private String userName;
    private String userAvatarUrl;
    private String userAccount;
    private String userEmail;

    /**
     * 构造函数
     * @param sysUser 使用完全体的Uer对象
     */
    public SysUserDTO(SysUser sysUser) {
        this.userId = sysUser.getUserId();
        this.userName = sysUser.getUserName();
        this.userAvatarUrl = sysUser.getUserAvatarUrl();
        this.userAccount = sysUser.getUserAccount();
        this.userEmail = sysUser.getUserEmail();
    }

}
