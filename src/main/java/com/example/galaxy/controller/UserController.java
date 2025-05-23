package com.example.galaxy.controller;

import com.example.galaxy.VO.ResultVO;
import com.example.galaxy.common.utils.ResultVOUtils;
import com.example.galaxy.common.utils.SecurityUtils;
import com.example.galaxy.entity.DTO.ChangePasswordDTO;
import com.example.galaxy.entity.DTO.RegisterDTO;
import com.example.galaxy.entity.DTO.SysUserDTO;
import com.example.galaxy.entity.SysRole;
import com.example.galaxy.entity.SysUser;
import com.example.galaxy.service.inter.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final ISysUserService iSysUserService;

    @Autowired
    public UserController(ISysUserService iSysUserService, SysUserDTO sysUserDTO) {
        this.iSysUserService = iSysUserService;
    }
    @PostMapping("/register")
    public <T> ResultVO<T> logUpUser(@Valid @RequestBody RegisterDTO registerDTO) {
        // 1. 校验账号是否已存在
        String userAccount = registerDTO.getUserAccount();
        if (iSysUserService.userExists(userAccount)>0) {
            return ResultVOUtils.failed("该账号已存在，请更换账号");
        }

        // 2. 创建用户对象并插入数据库
        SysUser sysUser = new SysUser(registerDTO);
        sysUser.setUserPassword(iSysUserService.encodePassword(sysUser.getUserPassword())); // 加密密码

        int result = iSysUserService.insertUser(sysUser);
        if (result == 1) {
            return ResultVOUtils.success("注册成功");
        } else {
            return ResultVOUtils.failed("用户注册失败");
        }
    }
    @PostMapping("/changeuser")
    public <T> ResultVO<T> changeUser(SysUserDTO sysUserDTO) {
        SysUser sysUser = new SysUser(sysUserDTO);
        if(iSysUserService.updateUser(sysUser) == 1){
            return ResultVOUtils.success();
        }else{
            return ResultVOUtils.failed("用户信息修改失败");
        }
    }
    @PostMapping("/changepassword")
    public <T> ResultVO<T> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        // 1. 获取当前登录用户
        SysUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getUserAccount() == null) {
            return ResultVOUtils.failed("找不到当前登录用户");
        }

        String userAccount = currentUser.getUserAccount();

        // 2. 校验新密码与确认密码是否一致
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            return ResultVOUtils.failed("新密码与确认密码不一致");
        }

        // 3. 获取数据库中的用户信息
        SysUser dbUser = iSysUserService.getUserByAccount(userAccount);
        if (dbUser == null) {
            return ResultVOUtils.failed("用户不存在");
        }

        // 4. 验证旧密码是否正确
        if (!iSysUserService.matches(changePasswordDTO.getOldPassword(), dbUser.getUserPassword())) {
            return ResultVOUtils.failed("旧密码错误");
        }

        // 5. 更新密码（使用加密后的密码）
        dbUser.setUserPassword(iSysUserService.encodePassword(changePasswordDTO.getNewPassword()));

        // 6. 保存更新
        if (iSysUserService.updateUser(dbUser) == 1) {
            return ResultVOUtils.success("密码修改成功");
        } else {
            return ResultVOUtils.failed("密码修改失败");
        }
    }
    @GetMapping("/permissions")
    public ResultVO<List<String>> getUserPermissions() {
        SysUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getUserAccount() == null) {
            return ResultVOUtils.failed("找不到当前登录用户");
        }
        List<String> permissions = Collections.singletonList(iSysUserService.getPermissionsByUserAccount(currentUser.getUserName()).stream()
                .toString());
        return ResultVOUtils.success(permissions);
    }
    /**
     * 获取当前登录用户的角色列表
     * @return 角色列表
     */
    @GetMapping("/roles")
    public ResultVO<List<String>> getUserRoles() {
        SysUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null || currentUser.getUserAccount() == null) {
            return ResultVOUtils.failed("找不到当前登录用户");
        }
        List<String> roles = iSysUserService.getRoles(currentUser.getUserAccount())
                .stream()
                .map(SysRole::getRoleName)
                .collect(Collectors.toList());
        return ResultVOUtils.success(roles);
    }
    /**
     * 为用户分配角色
     * @param userAccount 用户账号
     * @param roleId 角色ID
     * @return 是否成功
     */
    @PostMapping("/assign-role")
    public <T> ResultVO<T> assignRoleToUser(@RequestParam String userAccount, @RequestParam Long roleId) {
        boolean success = iSysUserService.assignRoleToUser(userAccount, roleId);
        if (success) {
            return ResultVOUtils.success("角色分配成功");
        } else {
            return ResultVOUtils.failed("角色分配失败");
        }
    }
}
