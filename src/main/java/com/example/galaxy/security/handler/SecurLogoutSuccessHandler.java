//退出成功管理器
package com.example.galaxy.security.handler;

import com.example.galaxy.common.utils.RedisUtils;
import com.example.galaxy.common.utils.RedisUtilsImpl;
import com.example.galaxy.VO.JSONAuthentication;
import com.example.galaxy.VO.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class SecurLogoutSuccessHandler extends JSONAuthentication implements LogoutSuccessHandler {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        // 获取用户 token
        String header = "Authorization";
        String token = request.getHeader(header);

        if (token != null) {
            // 清理 Redis 中的 token
            redisUtils.delete(token);
        }

        // 返回退出成功的响应
        ResponseStructure<String> data = ResponseStructure.success("退出成功");
        super.WriteJSON(request, response, data);
    }
}