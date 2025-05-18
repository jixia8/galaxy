package com.example.galaxy.service.inter;

import com.example.galaxy.entity.SysPermission;

import java.util.List;

public interface SysPermissionService {
    SysPermission getPermissionById(Long permissionId); // 根据权限ID查询权限
    List<SysPermission> getAllPermissions(); // 查询所有权限
    boolean addPermission(SysPermission permission); // 添加权限
    boolean updatePermission(SysPermission permission); // 更新权限
    boolean deletePermissionById(Long permissionId); // 根据权限ID删除权限
}
