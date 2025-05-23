package com.example.galaxy.controller;

import com.example.galaxy.VO.ResultVO;
import com.example.galaxy.common.utils.ResultVOUtils;
import com.example.galaxy.entity.SysPermission;
import com.example.galaxy.service.inter.SysPermissionService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 权限管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/permission")
public class UserPermissionController {
    private final SysPermissionService sysPermissionService;

    @Autowired
    public UserPermissionController(SysPermissionService sysPermissionService) {
        this.sysPermissionService = sysPermissionService;
    }

    /**
     * 分页查询权限列表
     */

    @GetMapping("/list")
    public ResultVO<PageInfo<SysPermission>> getPermissionList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        PageInfo<SysPermission> pageInfo = sysPermissionService.getPermissionPage(pageNum, pageSize, keyword == null ? "" : keyword);
        return ResultVOUtils.success(pageInfo);
    }
    /**
     * 根据 ID 查询权限详情
     */
    @GetMapping("/{id}")
    public ResultVO<SysPermission> getPermissionById(@PathVariable Long id) {
        SysPermission permission = sysPermissionService.getPermissionById(id);
        if (permission == null) {
            return ResultVOUtils.failed("权限不存在");
        }
        return ResultVOUtils.success(permission);
    }

    /**
     * 添加权限
     */
    @PostMapping("/add")
    public ResultVO<String> addPermission(@RequestBody SysPermission permission) {
        boolean success = sysPermissionService.addPermission(permission);
        if (success) {
            return ResultVOUtils.success("权限添加成功");
        } else {
            return ResultVOUtils.failed("权限添加失败");
        }
    }

    /**
     * 修改权限信息
     */
    @PutMapping("/update")
    public ResultVO<String> updatePermission(@RequestBody SysPermission permission) {
        boolean success = sysPermissionService.updatePermission(permission);
        if (success) {
            return ResultVOUtils.success("权限更新成功");
        } else {
            return ResultVOUtils.failed("权限更新失败");
        }
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/delete/{id}")
    public ResultVO<String> deletePermission(@PathVariable Long id) {
        boolean success = sysPermissionService.deletePermissionById(id);
        if (success) {
            return ResultVOUtils.success("权限删除成功");
        } else {
            return ResultVOUtils.failed("权限删除失败");
        }
    }
}
