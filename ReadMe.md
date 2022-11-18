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
