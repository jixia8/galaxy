package com.example.galaxy.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface RolePermissionMapper {
    public List<Long> getPermissionIdByUserId(Long roleId);
}
