package com.example.galaxy.service.impl;

import com.example.galaxy.service.inter.ISysUserService;
import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.entity.UserMenu;
import com.example.galaxy.mapper.UserMenuMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserMenuServiceImplTest {

    @Mock
    private UserMenuMapper userMenuMapper;

    @Mock
    private ISysUserService iSysUserService;

    @InjectMocks
    private UserMenuServiceImpl userMenuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMenusByUserAccount() {
        String userAccount = "testUser";

        // 模拟权限
        SysPermission permission1 = new SysPermission();
        permission1.setPermissionUrl("/menu1");
        SysPermission permission2 = new SysPermission();
        permission2.setPermissionUrl("/menu2");

        when(iSysUserService.getPermissionsByUserAccount(userAccount))
                .thenReturn(Arrays.asList(permission1, permission2));

        // 模拟菜单
        UserMenu menu1 = new UserMenu();
        menu1.setUserMenuPath("/menu1");
        UserMenu menu2 = new UserMenu();
        menu2.setUserMenuPath("/menu2");
        UserMenu menu3 = new UserMenu();
        menu3.setUserMenuPath("/menu3");

        when(userMenuMapper.getAllUserMenu()).thenReturn(Arrays.asList(menu1, menu2, menu3));

        // 调用方法
        List<UserMenu> result = userMenuService.getMenusByUserAccount(userAccount);

        // 验证结果
        assertEquals(2, result.size());
        assertEquals("/menu1", result.get(0).getUserMenuPath());
        assertEquals("/menu2", result.get(1).getUserMenuPath());

        // 验证交互
        verify(iSysUserService, times(1)).getPermissionsByUserAccount(userAccount);
        verify(userMenuMapper, times(1)).getAllUserMenu();
    }
}