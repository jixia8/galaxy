package com.example.galaxy.security.handler;

import com.example.galaxy.service.inter.ISysUserService;
import com.example.galaxy.common.utils.JwtTokenUtils;
import com.example.galaxy.common.utils.RedisUtils;
import com.example.galaxy.VO.JSONAuthentication;
import com.example.galaxy.VO.R;
import com.example.galaxy.VO.ResponseStructure;
import com.example.galaxy.entity.UserMenu;
import com.example.galaxy.security.service.impl.AuthUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.galaxy.common.Constants.TOKEN_KEY;
import static com.example.galaxy.common.Constants.VISIT_USER_KEY;

@Component
@Slf4j
public class SecurAuthenticationSuccessHandler extends JSONAuthentication implements AuthenticationSuccessHandler {

    @Autowired
    private ISysUserService service;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof AuthUserDetails)) {
            log.error("Principal 类型错误: {}", principal.getClass().getName());
            this.WriteJSON(request, response, R.failed("认证信息错误"));
            return;
        }

        AuthUserDetails authUserDetails = (AuthUserDetails) principal;
        String username = authUserDetails.getUsername();

        // 安全上下文设置认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String redisKey = TOKEN_KEY + username;
        String token = Optional.ofNullable(redisUtils.get(redisKey)).map(Object::toString).orElse("");

        if (token.isEmpty()) {
            log.info("初次登录，生成新 token...");
            token = jwtTokenUtils.generateToken(authUserDetails);
            redisUtils.set(redisKey, token, 3600L * 11);
        }

        // 记录访问者信息
        redisUtils.sSetAndTime(VISIT_USER_KEY, 60 * 60 * 24, username + System.currentTimeMillis());

        // 加载前端菜单
        List<UserMenu> menus;
        try {
            menus = service.getUserMenus(authUserDetails.getSysUser().getUserAccount());
        } catch (Exception e) {
            log.error("获取用户菜单失败", e);
            this.WriteJSON(request, response, R.failed("获取用户菜单失败"));
            return;
        }

        // 封装响应数据
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("auth", authUserDetails.getAuthorities());
        map.put("menus", menus);
        map.put("token", token);

        ResponseStructure<Map<String, Object>> data = ResponseStructure.success(map);
        this.WriteJSON(request, response, data);
    }
}