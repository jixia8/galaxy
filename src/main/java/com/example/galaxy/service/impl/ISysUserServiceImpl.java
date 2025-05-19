package com.example.galaxy.service.impl;

import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.entity.SysRole;
import com.example.galaxy.entity.SysUser;
import com.example.galaxy.entity.UserMenu;
import com.example.galaxy.mapper.*;
import com.example.galaxy.service.inter.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ISysUserServiceImpl implements ISysUserService {
    private final SysUserMapper sysUserMapper;
    private final UserRoleMapper userRoleMapper;
    private final SysRoleMapper sysRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final SysPermissionMapper sysPermissionMapper;
    private final UserMenuMapper userMenuMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ISysUserServiceImpl(SysUserMapper sysUserMapper,
                               UserRoleMapper userRoleMapper,
                               SysRoleMapper sysRoleMapper,
                               RolePermissionMapper rolePermissionMapper,
                               SysPermissionMapper sysPermissionMapper,
                               UserMenuMapper userMenuMapper,
                               BCryptPasswordEncoder passwordEncoder) {
        this.sysUserMapper = sysUserMapper;
        this.userRoleMapper = userRoleMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
        this.sysPermissionMapper = sysPermissionMapper;
        this.userMenuMapper = userMenuMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int updateUserByAccount(SysUser sysUser) {
        return sysUserMapper.updateUserByAccountSelective(sysUser);
    }
    @Override
    public boolean checkLogin(String account, String password) {
        SysUser sysUser = sysUserMapper.getUserByAccount(account);
        return sysUser != null && passwordEncoder.matches(password, sysUser.getUserPassword());
    }

    @Override
    public SysUser getUserByAccount(String account) {
        return sysUserMapper.getUserByAccount(account);
    }

    @Override
    public List<SysRole> getRoles(String account) {
        return userRoleMapper.getRoleIdByUserId(getUserByAccount(account).getUserId()).stream()
                .map(sysRoleMapper::selectRoleById)
                .filter(role -> role != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<SysPermission> getPermissionsByUserAccount(String account) {
        return getRoles(account).stream()
                .flatMap(role -> rolePermissionMapper.getPermissionIdByUserId(role.getRoleId()).stream())
                .map(sysPermissionMapper::selectByPermissionId)
                .filter(permission -> permission != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getUserPermissionIds(String userAccount) {
        return getPermissionsByUserAccount(userAccount).stream()
                .map(SysPermission::getPermissionId)
                .collect(Collectors.toList());
    }


    @Override
    public List<UserMenu> getUserMenus(String userAccount) {
        // 获取用户权限 ID 并转为 Set
        List<Long> userPermissions = getPermissionsByUserAccount(userAccount).stream()
                .map(SysPermission::getPermissionId)
                .collect(Collectors.toList());
        // 在数据库中直接过滤出用户有权限的菜单
        return userPermissions.stream()
                .flatMap(permissionId -> userMenuMapper.getMenusByPermissionIds(permissionId).stream())
                .collect(Collectors.toList());
    }
    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}