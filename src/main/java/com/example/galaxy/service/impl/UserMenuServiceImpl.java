package com.example.galaxy.service.impl;

import com.example.galaxy.service.inter.ISysUserService;
import com.example.galaxy.service.inter.UserMenuService;
import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.entity.UserMenu;
import com.example.galaxy.mapper.UserMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserMenuServiceImpl implements UserMenuService {
    private final UserMenuMapper userMenuMapper;
    private final ISysUserService iSysUserService;
    @Autowired
    public UserMenuServiceImpl(UserMenuMapper userMenuMapper, ISysUserService iSysUserService) {
        this.userMenuMapper = userMenuMapper;
        this.iSysUserService = iSysUserService;
    }

    @Override
    public List<UserMenu> getMenusByUserAccount(String userAccount) {
        // 获取用户权限 ID 并转为 Set
        Set<Long> userPermissions = iSysUserService.getPermissionsByUserAccount(userAccount).stream()
                .map(SysPermission::getPermissionId)
                .collect(Collectors.toSet());

        // 在数据库中直接过滤出用户有权限的菜单
        return userPermissions.stream()
                .flatMap(permissionId -> userMenuMapper.getMenusByPermissionIds(permissionId).stream())
                .collect(Collectors.toList());
    }
    @Override
    public int addUserMenu(UserMenu userMenu) {
        return userMenuMapper.insertUserMenu(userMenu);
    }

    @Override
    public int updateUserMenu(UserMenu userMenu) {
        return userMenuMapper.updateUserMenu(userMenu);
    }

    @Override
    public int deleteUserMenuById(long userMenuId) {
        return userMenuMapper.deleteUserMenuById(userMenuId);
    }

    @Override
    public UserMenu getUserMenuById(long userMenuId) {
        return userMenuMapper.getUserMenuById(userMenuId);
    }
}