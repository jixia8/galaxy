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

    public List<UserMenu> getMenusByUserAccount(String userAccount) {
        List<String> userPermissions= new ArrayList<>();
        for (SysPermission permission : iSysUserService.getPermissionsByUserAccount(userAccount)) {
            userPermissions.add(permission.getPermissionUrl());
        }
        // 查询所有菜单
        List<UserMenu> allMenus = userMenuMapper.getAllUserMenu();
        // 根据权限过滤菜单
        return allMenus.stream()
                .filter(menu -> userPermissions.contains(menu.getUserMenuPath())) // 假设权限与菜单路径关联
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