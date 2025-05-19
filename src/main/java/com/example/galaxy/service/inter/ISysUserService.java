
package com.example.galaxy.service.inter;

import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.entity.SysRole;
import com.example.galaxy.entity.SysUser;
import com.example.galaxy.entity.UserMenu;

import java.util.List;
public interface  ISysUserService {

    /**
     * 根据账号修改用户信息
     */
    int updateUserByAccount(SysUser sysUser);
    /**
     * 根据账号查询用户
     *
     * @param account 账号
     * @return SysUser
     */
    SysUser getUserByAccount(String account);

    /**
     * 获取用户权限（字符串形式）
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
    // 验证密码
    boolean matches(String rawPassword, String encodedPassword);

}