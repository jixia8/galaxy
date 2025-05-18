//用户退出管理器
package com.example.galaxy.security.handler;

import com.example.galaxy.VO.JSONAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurLogoutHandler extends JSONAuthentication implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String header = "Authorization";
        String headerToken = request.getHeader(header);
        if (StringUtils.hasText(headerToken)) {
            SecurityContextHolder.clearContext();
        }
    }
}