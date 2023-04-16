package com.chen.shiro.config;

import com.chen.shiro.ShiroRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author cgh
 * @create 2023-04-10
 * 整合shiro框架配置的相关类
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private ShiroRealm realm;


    //1.创建shiroFilter 负责拦截所有请求
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactory(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        shiroFilterFactoryBean.setLoginUrl("/auth/loginByShiro");

        //配置不会被拦截的链接 顺序判断
        Map<String, String> filterChainMap = new LinkedHashMap<>();
        filterChainMap.put("/auth/login", "anon"); //登录接口排除
        filterChainMap.put("/auth/loginByShiro", "anon"); //登录接口排除
        filterChainMap.put("/auth/registryByShiro", "anon"); //登录接口排除
        filterChainMap.put("/user/get/token", "anon"); //登录接口排除
        //拦截除上面的所有请求，都需要登录
        filterChainMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }

    //2.配置WebSecurityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }


}
