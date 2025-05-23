package com.example.galaxy.service.impl;

import com.example.galaxy.service.inter.ISysUserService;
import com.example.galaxy.service.inter.UserMenuService;
import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.entity.UserMenu;
import com.example.galaxy.mapper.UserMenuMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserMenuServiceImpl implements UserMenuService {
    private final UserMenuMapper userMenuMapper;
    private final ISysUserService iSysUserService;
    @Lazy
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
    @Override
    public List<UserMenu> getMenusByPermissionIds(long permissionId){
        return userMenuMapper.getMenusByPermissionIds(permissionId);
    }
    @Override
    public PageInfo<UserMenu> getMenuPage(int pageNum, int pageSize, String keyword) {
        try (Page<?> ignored = PageHelper.startPage(pageNum, pageSize)) {
            List<UserMenu> menus = userMenuMapper.selectMenusByKeyword(keyword);
            return new PageInfo<>(menus);
        }
    }
}