package com.example.galaxy.service.impl;

import com.example.galaxy.entity.SysRole;
import com.example.galaxy.mapper.SysRoleMapper;
import com.example.galaxy.service.inter.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    private final SysRoleMapper sysRoleMapper;
    @Autowired
    public SysRoleServiceImpl(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public SysRole getRoleById(Long roleId) {
        return sysRoleMapper.selectRoleById(roleId);
    }

    @Override
    public List<SysRole> getAllRoles() {
        return sysRoleMapper.selectAllRoles();
    }

    @Override
    public boolean addRole(SysRole role) {
        return sysRoleMapper.insertRole(role) > 0;
    }

    @Override
    public boolean updateRole(SysRole role) {
        return sysRoleMapper.updateRole(role) > 0;
    }

    @Override
    public boolean deleteRoleById(Long roleId) {
        return sysRoleMapper.deleteRoleById(roleId) > 0;
    }
}