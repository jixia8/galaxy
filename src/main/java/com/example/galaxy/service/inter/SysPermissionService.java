package com.example.galaxy.service.inter;

import com.example.galaxy.entity.SysPermission;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SysPermissionService {
    SysPermission getPermissionById(Long permissionId); // 根据权限ID查询权限
    List<SysPermission> getAllPermissions(); // 查询所有权限
    boolean addPermission(SysPermission permission); // 添加权限
    boolean updatePermission(SysPermission permission); // 更新权限
    boolean deletePermissionById(Long permissionId); // 根据权限ID删除权限

    /**
     * 根据分页获取权限列表
     * @param pageNum 页数
     * @param pageSize 页面大小
     * @return 权限列表
     */
    PageInfo<SysPermission> getPermissionPage(int pageNum, int pageSize, String keyword);
}
