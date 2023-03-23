package com.chen.filters;

import com.chen.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cgh
 * @create 2023-03-23
 * 自定义拦截器
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Value("${token.secret}")
    private String secret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        log.info("request intercepted");
        try {
            //校验token的有效性，任何校验上的错误都会抛出异常。
            JWTUtils.verifyToken(token, secret);
        } catch (Exception e) {
            log.warn("token exception:{}", e.getMessage());
            throw new RuntimeException(e);
        }
        //true放行
        return true;
    }
}
