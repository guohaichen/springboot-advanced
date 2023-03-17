### 跨域配置

>浏览器的同源策略：js脚本在未经允许的情况下，不能够访问其他域下的内容。
>
>同源：协议，域名，端口都相同的则是同源，其中一个不同则不属于同源。

**同源策略主要限制三个方面**

1. 一个域下的js脚本不能访问另一个域下面的cookie，localStorage和indexDB
2. 一个域下的脚本不能操作另一个域下的DOM
3. ajax不能跨域请求

#### 一、后端解决

1. 方式一：编写配置类，实现WebMvcConfigurer接口，重写addCorsMappings方法；

```java
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
}
```

2.方式二：使用过滤器来处理跨域请求

```java
@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }
```

3. 方式三：请求的目标映射方法上加上**@CrossOrigin**

#### 二、前端解决



### spring-security

#### 项目整合

> 参考https://www.bezkoder.com/spring-boot-vue-js-authentication-jwt-spring-security/



#### security流程

##### 一、springboot项目中整合

- 引入依赖spring-boot-starter-security，整合了依赖之后，再次访问http请求就会被拦截到/login页面

  - 输入用户名user，密码在后台xxx;

- 更改从数据库中查询用户名和密码进行登录；

  - com.chen.security.login.service.LoginUser实现UserDetails接口，构造注入一个User；getPassword和getUsername返回User的username和password；
- 实现UserDetailsService接口，重写loadUserByUsername方法；
  
```java
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserMapper userMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
    log.debug("user:{}",user);
    return new LoginUser(user);
  }
}
```

- 配置SecurityFilterChain

```java
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
  httpSecurity.authorizeRequests()
    //放行/user/get请求，放行的必须卸载anyRequest().authenticated()前面
    .mvcMatchers("/user/get").permitAll()
    .anyRequest().authenticated()
    .and()
    //开启login页面
    .formLogin()
    //登录成功处理器
    .successHandler(new MyAuthenticationSuccessHandler())
    //登陆失败处理器
    .failureHandler(new MyAuthenticationFailureHandler());
  return httpSecurity.build();
}
```



![spring-boot-authentication-spring-security-architecture](ReadMe.assets/spring-boot-authentication-spring-security-architecture.png)

#### security整合vue,vuex

![vuejs-jwt-authentication-vuex-project-overview](ReadMe.assets/vuejs-jwt-authentication-vuex-project-overview.png)
