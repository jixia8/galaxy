//身份校验失败处理器
package com.example.galaxy.security.handler;

import com.example.galaxy.common.enums.ResultCode;
import com.example.galaxy.VO.JSONAuthentication;
import com.example.galaxy.VO.ResponseStructure;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecurAuthenticationEntryPoint extends JSONAuthentication implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        ResponseStructure<Object> data = ResponseStructure.instance(ResultCode.UNAUTHORIZED.getCode(),"token不可用或已过期");
        this.WriteJSON(request, response, data);
    }
}
