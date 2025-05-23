package com.example.galaxy.service.impl;

import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.entity.SysRole;
import com.example.galaxy.entity.SysUser;
import com.example.galaxy.entity.UserMenu;
import com.example.galaxy.mapper.SysUserMapper;
import com.example.galaxy.service.inter.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j

public class ISysUserServiceImpl implements ISysUserService {

    private final SysUserMapper sysUserMapper;
    private final UserRoleService userRoleService;
    private final SysRoleService roleService;
    private final RolePermissionService rolePermissionService;
    private final SysPermissionService permissionService;
    private final UserMenuService userMenuService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ISysUserServiceImpl(SysUserMapper sysUserMapper,
                               UserRoleService userRoleService,
                               SysRoleService roleService,
                               RolePermissionService rolePermissionService,
                               SysPermissionService permissionService,
                               UserMenuService userMenuService,
                               BCryptPasswordEncoder passwordEncoder) {
        this.sysUserMapper = sysUserMapper;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
        this.rolePermissionService = rolePermissionService;
        this.permissionService = permissionService;
        this.userMenuService = userMenuService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 更新用户信息
     */
    @Override
    public int updateUser(SysUser sysUser) {
        return sysUserMapper.updateUserByAccountSelective(sysUser);
    }

    /**
     * 检查登录是否正确
     */
    @Override
    public boolean checkLogin(String account, String password) {
        SysUser sysUser = sysUserMapper.getUserByAccount(account);
        return sysUser != null && passwordEncoder.matches(password, sysUser.getUserPassword());
    }

    /**
     * 获取用户对象
     */
    @Override
    public SysUser getUserByAccount(String account) {
        return sysUserMapper.getUserByAccount(account);
    }

    /**
     * 获取用户角色
     */
    @Override
    public List<SysRole> getRoles(String account) {
        Long userId = sysUserMapper.getUserByAccount(account).getUserId();
        return userRoleService.getRoleIdByUserId(userId).stream()
                .map(roleService::getRoleById)
                .filter(role -> role != null)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户权限列表
     */
    @Override
    public List<SysPermission> getPermissionsByUserAccount(String account) {
        return getRoles(account).stream()
                .flatMap(role -> rolePermissionService.getPermissionIdsByRoleId(role.getRoleId()).stream())
                .map(permissionService::getPermissionById)
                .filter(permission -> permission != null)
                .collect(Collectors.toList());
    }
    /**
     * 获取用户权限 ID 列表
     */
    @Override
    public Set<Long> getPermissionIdsByUserAccount(String userAccount) {
        return getPermissionsByUserAccount(userAccount).stream()
                .map(SysPermission::getPermissionId)
                .collect(Collectors.toSet());
    }
    /**
     * 获取用户权限 ID 列表
     */
    @Override
    public List<Long> getUserPermissionIds(String userAccount) {
        return getPermissionsByUserAccount(userAccount).stream()
                .map(SysPermission::getPermissionId)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户菜单
     */
    @Override
    public List<UserMenu> getUserMenus(String userAccount) {
        List<Long> permissionIds = getUserPermissionIds(userAccount);
        return permissionIds.stream()
                .flatMap(pid -> userMenuService.getMenusByPermissionIds(pid).stream())
                .collect(Collectors.toList());
    }

    /**
     * 密码加密
     */
    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 注册用户
     */
    @Override
    public int insertUser(SysUser sysUser) {
        return sysUserMapper.insertUser(sysUser);
    }

    /**
     * 校验密码
     */
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 判断用户是否存在
     */
    @Override
    public int userExists(String account) {
        return sysUserMapper.countUserByAccount(account);
    }

    @Override
    public int countUserByAccount(String account) {
        return sysUserMapper.countUserByAccount(account);
    }
    @Override
    public long  getUserIdByAccount(String userAccount) {
        return sysUserMapper.getUserByAccount(userAccount).getUserId();
    }
    @Override
    public boolean assignRoleToUser(String userAccount, Long roleId) {
        // 1. 根据 userAccount 获取用户信息
        SysUser sysUser = getUserByAccount(userAccount);
        if (sysUser == null) {
            log.warn("用户不存在：{}", userAccount);
            return false;
        }

        // 2. 检查角色是否存在（可选）
        SysRole sysRole = roleService.getRoleById(roleId);
        if (sysRole == null) {
            log.warn("角色不存在：{}", roleId);
            return false;
        }

        // 3. 调用 DAO/Repository 层将角色分配给用户
        try {
            userRoleService.assignRoleToUser(sysUser.getUserId(), roleId);
            log.info("角色 {} 成功分配给用户 {}", roleId, userAccount);
            return true;
        } catch (Exception e) {
            log.error("角色分配失败：用户 {}, 角色 {}", userAccount, roleId, e);
            return false;
        }
    }
    @Override
    public PageInfo<SysUser> listUsers(int pageNum, int pageSize, String keyword) {
        try (Page<?> ignored = PageHelper.startPage(pageNum, pageSize)) {
            List<SysUser> users = sysUserMapper.selectUsersByKeyword(keyword);
            return new PageInfo<>(users);
        }
    }
}