package com.example.galaxy.mapper;
import com.example.galaxy.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UserRoleMapper {
    public List<Long> getRoleIdByUserId(Long userId);
    public int addUserRole(@Param("userId")long userId, @Param("roleId")long roleId);
    //一次为用户插入多个角色
    int addUserRoles(@Param("userId") long userId, @Param("roleIds") List<Long> roleIds);
}
