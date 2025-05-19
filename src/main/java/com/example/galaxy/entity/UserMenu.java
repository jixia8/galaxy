package com.example.galaxy.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMenu {
    /**
     * 与对应的permissionId相等
     * 与数据库对应menuId
     */
    private long userMenuId;                // 菜单ID
    private String userMenuName;            // 菜单名称
    private String userMenuPath;            // 路由路径
    private String userMenuComponent;       // 前端组件路径
    private String userMenuIcon;            // 菜单图标
    private long parentId;        // 父菜单ID
}
