package com.example.galaxy.service.impl;

import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.entity.SysRole;
import com.example.galaxy.entity.SysUser;
import com.example.galaxy.mapper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ISysUserServiceImplTest {

    @Mock
    private SysUserMapper sysUserMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private SysRoleMapper sysRoleMapper;

    @Mock
    private RolePermissionMapper rolePermissionMapper;

    @Mock
    private SysPermissionMapper sysPermissionMapper;

    @Mock
    private UserMenuMapper userMenuMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private ISysUserServiceImpl sysUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckLogin_Success() {
        String account = "testUser";
        String password = "testPassword";
        SysUser mockUser = new SysUser();
        mockUser.setUserPassword("encodedPassword");

        when(sysUserMapper.getUserByAccount(account)).thenReturn(mockUser);
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);

        assertTrue(sysUserService.checkLogin(account, password));
        verify(sysUserMapper, times(1)).getUserByAccount(account);
        verify(passwordEncoder, times(1)).matches(password, "encodedPassword");
    }

    @Test
    void testCheckLogin_Failure() {
        String account = "testUser";
        String password = "testPassword";

        when(sysUserMapper.getUserByAccount(account)).thenReturn(null);

        assertFalse(sysUserService.checkLogin(account, password));
        verify(sysUserMapper, times(1)).getUserByAccount(account);
    }

    @Test
    void testGetRoles() {
        String account = "testUser";
        SysUser mockUser = new SysUser();
        mockUser.setUserId(1L);
        List<Long> roleIds = Arrays.asList(1L, 2L);
        SysRole role1 = new SysRole();
        SysRole role2 = new SysRole();

        when(sysUserMapper.getUserByAccount(account)).thenReturn(mockUser);
        when(userRoleMapper.getRoleIdByUserId(1L)).thenReturn(roleIds);
        when(sysRoleMapper.selectRoleById(1L)).thenReturn(role1);
        when(sysRoleMapper.selectRoleById(2L)).thenReturn(role2);

        List<SysRole> roles = sysUserService.getRoles(account);

        assertEquals(2, roles.size());
        verify(userRoleMapper, times(1)).getRoleIdByUserId(1L);
    }

    @Test
    void testGetPermissionsByUserAccount() {
        String account = "testUser";
        SysUser mockUser = new SysUser();
        mockUser.setUserId(1L);
        SysRole role = new SysRole();
        role.setRoleId(1L);
        List<Long> permissionIds = Arrays.asList(1L, 2L);
        SysPermission permission1 = new SysPermission();
        SysPermission permission2 = new SysPermission();

        // 模拟 getUserByAccount 返回值
        when(sysUserMapper.getUserByAccount(account)).thenReturn(mockUser);
        // 模拟 getRoles 返回值
        when(userRoleMapper.getRoleIdByUserId(1L)).thenReturn(Arrays.asList(1L));
        when(sysRoleMapper.selectRoleById(1L)).thenReturn(role);
        // 模拟权限相关方法
        when(rolePermissionMapper.getPermissionIdByUserId(1L)).thenReturn(permissionIds);
        when(sysPermissionMapper.selectByPermissionId(1L)).thenReturn(permission1);
        when(sysPermissionMapper.selectByPermissionId(2L)).thenReturn(permission2);

        List<SysPermission> permissions = sysUserService.getPermissionsByUserAccount(account);

        assertEquals(2, permissions.size());
        verify(rolePermissionMapper, times(1)).getPermissionIdByUserId(1L);
    }
    @Test
    void testEncodePassword() {
        String rawPassword = "testPassword";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        assertEquals(encodedPassword, sysUserService.encodePassword(rawPassword));
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }
}