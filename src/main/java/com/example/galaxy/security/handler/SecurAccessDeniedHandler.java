//权限管理器
package com.example.galaxy.security.handler;

import com.example.galaxy.VO.JSONAuthentication;
import com.example.galaxy.VO.ResponseStructure;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecurAccessDeniedHandler extends JSONAuthentication implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ResponseStructure<Object> data = ResponseStructure.failed("权限不足:"+accessDeniedException.getMessage());
        this.WriteJSON(request, response, data);
    }
}