//处理用户登录失败操作
package com.example.galaxy.security.handler;

import com.example.galaxy.common.enums.ResultCode;
import com.example.galaxy.VO.JSONAuthentication;
import com.example.galaxy.VO.ResponseStructure;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecurAuthenticationFailureHandler extends JSONAuthentication implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        ResponseStructure<Object> data = ResponseStructure.instance(ResultCode.UNAUTHORIZED.getCode(),"登录失败:"+"账号或密码错误");
        //输出
        this.WriteJSON(request, response, data);
    }
}
