package com.example.galaxy.service.inter;

import com.example.galaxy.entity.UserMenu;

import java.util.List;


public interface UserMenuService {
    List<UserMenu> getMenusByUserAccount(String userAccount);

    int addUserMenu(UserMenu userMenu);

    int updateUserMenu(UserMenu userMenu);

    int deleteUserMenuById(long userMenuId);

    UserMenu getUserMenuById(long userMenuId);
}