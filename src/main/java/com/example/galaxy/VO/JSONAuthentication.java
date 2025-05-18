package com.example.galaxy.VO;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JSONAuthentication {
    protected void WriteJSON(HttpServletRequest request,
                             HttpServletResponse response,
                             Object object) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(object));
    }
}
