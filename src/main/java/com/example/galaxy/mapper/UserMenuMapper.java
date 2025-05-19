package com.example.galaxy.mapper;

import com.example.galaxy.entity.UserMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMenuMapper {
    UserMenu getUserMenuById(Long userId);
    /**
     * 查询所有用户菜单，可能存在性能问题
     * @return UserMenu集合
     */
    List<UserMenu> getAllUserMenu();
    int insertUserMenu(UserMenu userMenu);
    int updateUserMenu(UserMenu userMenu);
    int deleteUserMenuById(Long userMenuId);
    List<UserMenu> getMenusByPermissionIds( Long permissionId);
}
