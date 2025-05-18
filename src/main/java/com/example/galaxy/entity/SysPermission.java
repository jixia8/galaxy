package com.example.galaxy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysPermission {
    private Long permissionId;
    private String permissionName;
    private String permissionUrl;
    private String permissionMethod;
}