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
    public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    
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

  - 

![spring-boot-authentication-spring-security-architecture](ReadMe.assets/spring-boot-authentication-spring-security-architecture.png)

#### security整合vue,vuex

![vuejs-jwt-authentication-vuex-project-overview](ReadMe.assets/vuejs-jwt-authentication-vuex-project-overview.png)
