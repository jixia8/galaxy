package com.example.galaxy.service.impl;

import com.example.galaxy.mapper.UserRoleMapper;
import com.example.galaxy.service.inter.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleMapper userRoleMapper;
    @Autowired
    public UserRoleServiceImpl(UserRoleMapper userRoleMapper)
    {
        this.userRoleMapper = userRoleMapper;
    }
    @Override
    public List<Long> getRoleIdByUserId(Long userId)
    {
        return userRoleMapper.getRoleIdByUserId(userId);
    }
    @Override
    public Boolean assignRoleToUser(long userId, long roleId)
    {
        return userRoleMapper.addUserRole(userId, roleId)>0;
    }
    @Override
    public Boolean assignRolesToUser(long userId, List<Long> roleIds){
        return userRoleMapper.addUserRoles(userId, roleIds)>0;
    }
}
