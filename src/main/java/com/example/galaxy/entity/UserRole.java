package com.example.galaxy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * SysUser和SysRole的中间表
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRole {
    private Long userId;
    private Long roleId;
}
