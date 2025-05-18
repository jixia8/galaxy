package com.example.galaxy.mapper;
import com.example.galaxy.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UserRoleMapper {
    public List<Long> getRoleIdByUserId(Long userId);
}
