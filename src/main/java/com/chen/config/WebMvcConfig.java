package com.chen.config;

import com.chen.interceptors.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import java.nio.charset.StandardCharsets;

/**
 * @author cgh
 * @create 2023-03-23
 * 注册token拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /*@Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**") //拦截所有请求
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/auth/loginByShiro")
                .excludePathPatterns("/auth/login"); //放行登录接口，否则...
    }*/

    /**
     * 映射图片url，因为图片是存在本地的，前端img src中如果是访问的本地资源的话，会报错，Not allowed to load local resource
     * 所以将本地资源file:F:/gitHub/blog/img/人像摄影/xxx.jpg   映射为 localhost:端口号/image/xxx.jpg
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:F:/gitHub/blog/img/rxsy");
    }

    /*更改程序映射请求路径默认策略,此配置可使用含中文http访问本地资源，不会报404*/
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper=new UrlPathHelper();
        urlPathHelper.setUrlDecode(false);
        urlPathHelper.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configurer.setUrlPathHelper(urlPathHelper);
    }
}
