package com.example.galaxy.security.service.impl;

import com.example.galaxy.entity.SysUser;
import com.example.galaxy.service.inter.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("authUserDetailsServiceImpl")
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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