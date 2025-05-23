package com.example.galaxy.common.utils;
import com.example.galaxy.entity.SysUser;
import com.example.galaxy.security.service.impl.AuthUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;
import java.util.List;

public class SecurityUtils {

    public static AuthUserDetails getCurrentUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof AuthUserDetails) {
            return (AuthUserDetails) auth.getPrincipal();
        }
        return null;
    }

    public static SysUser getCurrentUser() {
        AuthUserDetails details = getCurrentUserDetails();
        return details != null ? details.getSysUser() : null;
    }

    public static Long getCurrentUserId() {
        SysUser user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    public static List<Long> getCurrentUserPermissionIds() {
        AuthUserDetails details = getCurrentUserDetails();
        return details != null ? details.getPermissionIds() : Collections.emptyList(); // Java 8 兼容写法
    }
}