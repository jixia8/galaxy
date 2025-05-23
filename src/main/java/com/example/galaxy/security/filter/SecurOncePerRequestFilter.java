package com.example.galaxy.security.filter;

import com.example.galaxy.common.utils.JwtTokenUtils;
import com.example.galaxy.common.utils.JwtTokenUtilsImpl;
import com.example.galaxy.security.service.impl.AuthUserDetails;
import com.example.galaxy.security.service.impl.AuthUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class SecurOncePerRequestFilter extends OncePerRequestFilter {


    private final JwtTokenUtils jwtTokenUtils;
    private final String header = "Authorization";
    private final AuthUserDetailsServiceImpl userDetailsService;
    @Autowired
    public SecurOncePerRequestFilter(AuthUserDetailsServiceImpl authUserDetailsServiceImpl,  JwtTokenUtilsImpl jwtTokenUtilImpl) {
        this.userDetailsService = authUserDetailsServiceImpl;
        this.jwtTokenUtils = jwtTokenUtilImpl;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String headerToken = request.getHeader(header);
//        System.out.println("SecurOncePerRequestFilter is executing...");
//        System.out.println("Request URI: " + request.getRequestURI());
//        System.out.println("Servlet Path: " + request.getServletPath());
//        System.out.println("Request Method: " + request.getMethod());
//
//        // 如果是登录请求，直接放行
//        if ("/login".equals(request.getServletPath()) && HttpMethod.POST.matches(request.getMethod())) {
//            System.out.println("Login request detected, bypassing filter...");
//            chain.doFilter(request, response);
//            return;
//        }

        if (!StringUtils.isEmpty(headerToken)) {
            String token = headerToken.replace("Bearer", "").trim();
            boolean check = false;
            try {
                check = this.jwtTokenUtils.isTokenExpired(token);
            } catch (Exception e) {
                new Throwable("令牌已过期，请重新登录。" + e.getMessage());
            }
            if (!check) {
                //通过令牌获取用户名称
                String username = jwtTokenUtils.getUsernameFromToken(token);
//System.out.println("username = " + username);
                //判断用户不为空，且SecurityContextHolder授权信息还是空的
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //通过用户信息得到UserDetails
                    AuthUserDetails authuserDetails = userDetailsService.loadUserByUsername(username);
                    //验证令牌有效性
                    boolean validata = false;
                    try {
                        validata = jwtTokenUtils.validateToken(token, authuserDetails);
                    } catch (Exception e) {
                        new Throwable("验证token无效:" + e.getMessage());
                    }
                    if (validata) {
                        // 将用户信息存入 authentication，方便后续校验
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        authuserDetails,
                                        null,
                                        authuserDetails.getAuthorities()
                                );
                        //
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }
}