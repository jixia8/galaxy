package com.example.galaxy.service.inter;

import com.example.galaxy.entity.UserMenu;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserMenuService {
    List<UserMenu> getMenusByUserAccount(String userAccount);

    int addUserMenu(UserMenu userMenu);

    int updateUserMenu(UserMenu userMenu);

    int deleteUserMenuById(long userMenuId);

    UserMenu getUserMenuById(long userMenuId);

    List<UserMenu> getMenusByPermissionIds(long permissionId);

    PageInfo<UserMenu> getMenuPage(int pageNum, int pageSize, String keyword);
}