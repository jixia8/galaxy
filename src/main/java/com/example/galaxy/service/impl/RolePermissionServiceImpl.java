package com.example.galaxy.service.impl;

import com.example.galaxy.mapper.RolePermissionMapper;
import com.example.galaxy.service.inter.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    private final RolePermissionMapper rolePermissionMapper;
    @Autowired
    public RolePermissionServiceImpl(RolePermissionMapper rolePermissionMapper)
    {
        this.rolePermissionMapper = rolePermissionMapper;
    }
    @Override
    public List<Long> getPermissionIdsByRoleId(long roleId)
    {
        return rolePermissionMapper.getPermissionIdByRoleId(roleId);
    }
}
