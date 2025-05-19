//用户退出管理器
package com.example.galaxy.security.handler;

import com.example.galaxy.VO.JSONAuthentication;
import com.example.galaxy.common.utils.JwtTokenUtils;
import com.example.galaxy.common.utils.RedisUtils;
import com.example.galaxy.common.utils.RedisUtilsImpl;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.galaxy.common.Constants.TOKEN_KEY;
@Slf4j
@Component
public class SecurLogoutHandler extends JSONAuthentication implements LogoutHandler {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String header = "Authorization";
        String headerToken = request.getHeader(header);
        if (StringUtils.hasText(headerToken)) {
            // 去掉 Bearer 前缀
            String token = headerToken.replace("Bearer ", "").trim();
            String username = jwtTokenUtils.getUsernameFromToken(token); // 从 Token 中提取用户名
            if (username != null) {
                log.info("从 Token 中提取的用户名: {}", username);
                redisUtils.delete(TOKEN_KEY + username);
            } else {
                log.warn("无法从 Token 中提取用户名");
            }
        } else {
            log.warn("请求头中没有 Token");
        }
        SecurityContextHolder.clearContext();
    }
}