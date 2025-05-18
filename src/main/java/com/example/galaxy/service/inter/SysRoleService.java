package com.example.galaxy.service.inter;

import com.example.galaxy.entity.SysRole;

import java.util.List;

public interface SysRoleService {
        SysRole getRoleById(Long roleId); // 根据角色ID查询角色
        List<SysRole> getAllRoles(); // 查询所有角色
        boolean addRole(SysRole role); // 添加角色
        boolean updateRole(SysRole role); // 更新角色
        boolean deleteRoleById(Long roleId); // 根据角色ID删除角色

}
