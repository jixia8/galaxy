package com.example.galaxy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Role和Permission的中间表
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RolePermission {
    private Long roleId;
    private Long permissionId;
}
