package com.example.galaxy.service.impl;
import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.mapper.SysPermissionMapper;
import com.example.galaxy.service.inter.SysPermissionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public SysPermission getPermissionById(Long permissionId) {
        return sysPermissionMapper.selectByPermissionId(permissionId);
    }

    @Override
    public List<SysPermission> getAllPermissions() {
        return sysPermissionMapper.selectAll();
    }

    @Override
    public boolean addPermission(SysPermission permission) {
        return sysPermissionMapper.insert(permission) > 0;
    }

    @Override
    public boolean updatePermission(SysPermission permission) {
        return sysPermissionMapper.update(permission) > 0;
    }

    @Override
    public boolean deletePermissionById(Long permissionId) {
        return sysPermissionMapper.deleteById(permissionId) > 0;
    }

    @Override
    public PageInfo<SysPermission> getPermissionPage(int pageNum, int pageSize, String keyword) {
        try (Page<?> ignored = PageHelper.startPage(pageNum, pageSize)) {
            List<SysPermission> menus =sysPermissionMapper.selectByKeyword(keyword);
            return new PageInfo<>(menus);
        }
    }
}
