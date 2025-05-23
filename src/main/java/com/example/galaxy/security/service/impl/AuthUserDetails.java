package com.example.galaxy.security.service.impl;

import com.example.galaxy.entity.SysUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class AuthUserDetails implements UserDetails {
    @Getter
    private final SysUser sysUser;
    private final List<Long> permissionIds;

    public AuthUserDetails(SysUser sysUser, List<Long> permissionIds) {
        this.sysUser = sysUser;
        this.permissionIds = permissionIds;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissionIds.stream()
                .map(permissionId -> (GrantedAuthority) () -> "PERMISSION_" + permissionId)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return sysUser.getUserPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUserAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
        // return sysUser.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        // return sysUser.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        // return sysUser.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return sysUser.getEnabled();
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

}