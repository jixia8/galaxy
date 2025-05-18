package com.example.galaxy.entity;

import com.example.galaxy.service.inter.ISysUserService;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AuthUser implements UserDetails {
    // 获取原始用户对象，供业务使用
    private final SysUser sysUser;
    private final ISysUserService sysUserService;
    public AuthUser(SysUser sysUser, ISysUserService sysUserService) {
        this.sysUser = sysUser;
        this.sysUserService = sysUserService;
    }

    // 获取角色权限列表（自动添加 ROLE_ 前缀）
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SysRole> roles = sysUserService.getRoles(sysUser.getUserAccount());
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return sysUser.getUserPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUserName();
    }

    // 账号是否未过期（true 表示未过期）
    public String getUserAccount() {
        return sysUser.getUserAccount();
    }

    // 账号是否未过期（true 表示未过期）
    @Override
    public boolean isAccountNonExpired() {
        return sysUser.getAccountNonExpired() != null && sysUser.getAccountNonExpired();
    }

    // 账号是否未锁定
    @Override
    public boolean isAccountNonLocked() {
        return sysUser.getAccountNonLocked() != null && sysUser.getAccountNonLocked();
    }

    // 凭证是否未过期（例如密码过期）
    @Override
    public boolean isCredentialsNonExpired() {
        return sysUser.getCredentialsNonExpired() != null && sysUser.getCredentialsNonExpired();
    }

    // 是否启用
    @Override
    public boolean isEnabled() {
        return sysUser.getEnabled() != null && sysUser.getEnabled();
    }

}