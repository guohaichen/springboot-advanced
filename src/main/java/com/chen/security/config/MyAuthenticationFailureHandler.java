package com.chen.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cgh
 * @create 2022-08-12 15:33
 * 认证失败处理器
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Map<String,Object> result = new HashMap<>();
        result.put("msg","登陆失败,错误信息: "+exception.getMessage());
        result.put("code",500);
        //设置返回响应结果格式为json：
        response.setContentType("application/json;charset=UTF-8");
        String stringResult = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(stringResult);
    }
}
