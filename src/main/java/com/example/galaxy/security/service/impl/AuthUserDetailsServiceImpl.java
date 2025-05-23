package com.example.galaxy.security.service.impl;

import com.example.galaxy.entity.SysUser;
import com.example.galaxy.service.inter.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("authUserDetailsServiceImpl")
public class AuthUserDetailsServiceImpl implements UserDetailsService {
    private final ISysUserService userService;
    @Autowired
    public AuthUserDetailsServiceImpl(ISysUserService userService) {
        this.userService = userService;
    }

    /**
     * 根据用户账号加载用户信息
     * @param username 实际上是 useraccount
     * @return 自定义实现的 userdetails
     * @throws UsernameNotFoundException 未发现对应用户的错误
     */
    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userService.getUserByAccount(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 获取用户权限 ID
        List<Long> permissionIds = userService.getUserPermissionIds(username);

        // 返回自定义的 UserDetails 对象
        return new AuthUserDetails(sysUser, permissionIds);
    }
}