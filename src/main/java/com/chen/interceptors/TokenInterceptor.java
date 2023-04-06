package com.chen.interceptors;

import com.chen.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
//        log.info("request intercepted");
        if (token == null){
            return false;
        }
        //验证token是否过期
        String redisToken = redisTemplate.opsForValue().get(token);
        if (redisToken ==null){
            response.getWriter().write("{'code':500,'msg':''token已过期}'}");
            throw new RuntimeException("token已过期");
        }
        try {
            //校验token的有效性，任何校验上的错误都会抛出异常。
            JWTUtils.verifyToken(token, secret);
            //true放行
            return true;
        } catch (Exception e) {
            log.warn("token exception:{}", e.getMessage());
            //返回json数据
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{'code':500,'msg':''token校验失败}'}");
            throw new RuntimeException(e.getMessage());
        }
    }
}
