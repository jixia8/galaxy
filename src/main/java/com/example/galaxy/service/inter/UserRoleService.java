package com.example.galaxy.service.inter;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
public interface UserRoleService {
    List<Long> getRoleIdByUserId(Long userId);
    Boolean assignRoleToUser(long userId,long roleId);
    Boolean assignRolesToUser(long userId, List<Long> roleIds);
}
