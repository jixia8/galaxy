package com.example.galaxy.mapper;

import com.example.galaxy.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface SysPermissionMapper {
    SysPermission selectByPermissionId(Long permissionId);

    List<SysPermission> selectAll();

    int insert(SysPermission permission);

    int update(SysPermission permission);

    int deleteById(Long permissionId);

    List<SysPermission> selectByKeyword(@Param("keyword") String keyword);
}