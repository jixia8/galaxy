package com.example.galaxy.service.inter;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
public interface RolePermissionService {
     List<Long> getPermissionIdsByRoleId(long roleId);
}

