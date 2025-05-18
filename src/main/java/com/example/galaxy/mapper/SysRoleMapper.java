package com.example.galaxy.mapper;

import com.example.galaxy.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysRoleMapper {
    SysRole selectRoleById(Long roleId);
    List<SysRole> selectAllRoles();
    int insertRole(SysRole role);
    int updateRole(SysRole role);
    int deleteRoleById(Long roleId);
}