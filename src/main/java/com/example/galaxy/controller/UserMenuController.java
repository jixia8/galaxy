package com.example.galaxy.controller;

import com.example.galaxy.VO.ResultVO;
import com.example.galaxy.common.utils.ResultVOUtils;
import com.example.galaxy.entity.UserMenu;
import com.example.galaxy.service.inter.UserMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 菜单管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/menu")
public class UserMenuController {

    private final UserMenuService userMenuService;

    @Autowired
    public UserMenuController(UserMenuService userMenuService) {
        this.userMenuService = userMenuService;
    }

    /**
     * 分页获取菜单列表（支持关键字搜索）
     */
    @GetMapping("/list")
    public ResultVO<PageInfo<UserMenu>> getMenuList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        PageInfo<UserMenu> pageInfo = userMenuService.getMenuPage(pageNum, pageSize, keyword == null ? "" : keyword);
        return ResultVOUtils.success(pageInfo);
    }

    /**
     * 根据 ID 查询菜单详情
     */
    @GetMapping("/{id}")
    public ResultVO<UserMenu> getMenuById(@PathVariable Long id) {
        UserMenu menu = userMenuService.getUserMenuById(id);
        if (menu == null) {
            return ResultVOUtils.failed("菜单不存在");
        }
        return ResultVOUtils.success(menu);
    }

    /**
     * 添加新菜单
     */
    @PostMapping("/add")
    public ResultVO<String> addMenu(@RequestBody UserMenu menu) {
        userMenuService.addUserMenu(menu);
        if (userMenuService.addUserMenu(menu)>0) {
            return ResultVOUtils.success("菜单添加成功");
        } else {
            return ResultVOUtils.failed("菜单添加失败");
        }
    }

    /**
     * 更新菜单信息
     */
    @PutMapping("/update")
    public ResultVO<String> updateMenu(@RequestBody UserMenu menu) {
        if (userMenuService.updateUserMenu(menu)>0) {
            return ResultVOUtils.success("菜单更新成功");
        } else {
            return ResultVOUtils.failed("菜单更新失败");
        }
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/delete/{id}")
    public ResultVO<String> deleteMenu(@PathVariable Long id) {

        if (userMenuService.deleteUserMenuById(id)>0) {
            return ResultVOUtils.success("菜单删除成功");
        } else {
            return ResultVOUtils.failed("菜单删除失败");
        }
    }

    /**
     * 根据权限 ID 查询菜单
     */
    @GetMapping("/permission/{permissionId}")
    public ResultVO<?> getMenusByPermissionId(@PathVariable Long permissionId) {
        List<UserMenu> menus = userMenuService.getMenusByPermissionIds(permissionId);
        return ResultVOUtils.success(menus);
    }
}