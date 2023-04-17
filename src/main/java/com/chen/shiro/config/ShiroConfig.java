package com.chen.shiro.config;

import com.chen.shiro.JWTFilter;
import com.chen.shiro.ShiroRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;

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

        //添加自定义的jwtFilter，
        HashMap<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt",new JWTFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        //自定义url规则
        HashMap<String, String> filterRuleMap = new HashMap<>();
        filterRuleMap.put("/auth/loginByShiro","anon");
        filterRuleMap.put("/auth/registryByShiro","anon");
        filterRuleMap.put("/**","jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);

        /*//配置不会被拦截的链接 顺序判断
        Map<String, String> filterChainMap = new LinkedHashMap<>();
        filterChainMap.put("/auth/login", "anon"); //登录接口排除
        filterChainMap.put("/auth/loginByShiro", "anon"); //登录接口排除
        filterChainMap.put("/auth/registryByShiro", "anon"); //登录接口排除
//        filterChainMap.put("/user/get/token", "anon"); //测试接口排除
        //拦截除上面的所有请求，都需要登录
        filterChainMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);*/
        return shiroFilterFactoryBean;
    }

    //2.配置WebSecurityManager:加上自定义的realm，关闭session
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置
        defaultWebSecurityManager.setRealm(realm);
        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);

        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        return defaultWebSecurityManager;
    }


}
