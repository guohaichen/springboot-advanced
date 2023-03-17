//package com.chen.security.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
///**
// * @author cgh
// * @create 2022-08-11 10:04
// */
//@Configuration
//public class SecurityConfig {
//    //@Bean
//    // password加密bean
//    public PasswordEncoder passwordEncoder() {
//
//        return new BCryptPasswordEncoder();
//    }
//
//
////    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests()
//                //放行/user/get请求，放行的必须卸载anyRequest().authenticated()前面
//                .mvcMatchers("/user/get").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                //开启login页面
//                .formLogin()
//                //登录成功处理器
//                .successHandler(new MyAuthenticationSuccessHandler())
//                //登陆失败处理器
//                .failureHandler(new MyAuthenticationFailureHandler());
//
//
//                /*注销登录处理器
//                .and()
//                .logout().logoutSuccessHandler()*/
//
//        return httpSecurity.build();
//    }
//}
