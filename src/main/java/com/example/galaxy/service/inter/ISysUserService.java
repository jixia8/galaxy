
package com.example.galaxy.service.inter;

import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.entity.SysRole;
import com.example.galaxy.entity.SysUser;
import com.example.galaxy.entity.UserMenu;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Set;

public interface  ISysUserService {

    /**
     * 根据账号修改用户信息
     */
    int updateUser(SysUser sysUser);
    /**
     * 根据账号查询用户
     *
     * @param account 账号
     * @return SysUser
     */
    SysUser getUserByAccount(String account);

    /**
     *
     * 根据账号获取用户权限id（set）
     * @param userAccount 用户账号
     * @return Set<Long>
     */
    Set<Long> getPermissionIdsByUserAccount(String userAccount);
    /**
     * 获取用户权限（list）
     */
    List<Long> getUserPermissionIds(String userAccount);

    /**
     * 获取用户菜单（前端渲染用）
     */
    List<UserMenu> getUserMenus(String userAccount) throws Exception;

    /**
     * @param account  账号
     * @param password 密码
     * @return 查找是否成功
     */
    boolean checkLogin(String account, String password);

    /**
     * 获取用户角色
     *
     * @param account 账号
     * @return 角色列表
     */
    List<SysRole> getRoles(String account);

    /**
     * 获取用户权限
     *
     * @param account 用户账号
     * @return 返回所有权限列表
     */
    List<SysPermission> getPermissionsByUserAccount(String account);
    /**
     * 对发来的密码加密 使用bcrypt加密
     * @param rawPassword 发来的密码
     * @return 加密后的密码
     */
    String encodePassword(String rawPassword);
    /**
     * 插入用户
     * @param sysUser 用户信息
     * @return 插入成功返回1
     */
    int insertUser(SysUser sysUser);
    // 验证密码
    boolean matches(String rawPassword, String encodedPassword);

    /**
     * 查验用户是否存在
     * @param userAccount 用户账号
     * @return 是否存在
     */
    int userExists(String userAccount);

    /**
     * 根据账号获得id
     * @param userAccount 用户账号
     * @return 用户id
     */
    long  getUserIdByAccount(String userAccount);
    /**
     * 根据账号查询用户数量
     * @param account 用户账号
     * @return 用户数量
     */
    int countUserByAccount(String account);
    /**
     * 为用户分配角色
     * @param userAccount 用户账号
     * @param roleId 角色id
     * @return 是否分配成功
     */
    boolean assignRoleToUser(String userAccount, Long roleId);
    /**
     * 按业获取用户
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 用户列表
     */
    PageInfo<SysUser> listUsers(int offset, int limit, String keyword);
}