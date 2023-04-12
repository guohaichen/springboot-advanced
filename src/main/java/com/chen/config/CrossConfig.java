package com.chen.config;

/**
 * @author cgh
 * @create 2023-03-16 15:49
 * 浏览器同源策略：浏览器发送请求的协议、域名和端口要和服务器接收请求的协议、域名以及端口一致。
 * 前后端分离开发，一定是会使用两个端口，就出现了跨域问题。
 * 跨域配置，解决跨域问题，@CrossOrigin(origins = "*")加在方法上也可跨域问题。
 * 注意：springboot整合shiro后，默认所有请求会先经过shiro的监听器，所以下面的跨域配置就不管用了。
 */
/* 解决跨域方式一：
@Configuration
public class CrossConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}*/
// 方式二：

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Configuration
public class CrossConfig implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, UserInfo");
        response.setHeader("Access-Control-Max-Age", "3600");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}



