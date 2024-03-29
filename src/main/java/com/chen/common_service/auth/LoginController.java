package com.chen.common_service.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.UserLogin;
import com.chen.common_service.service.IUserLoginService;
import com.chen.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.chen.common_service.constant.UserConstant.EXPIRE_TIME_DAY;
import static com.chen.common_service.constant.UserConstant.USER_TOKEN_PREFIX;

@Slf4j
@RequestMapping("/auth")
@RestController
public class LoginController {
    //从配置文件获取salt;
    @Value("${encode.salt}")
    private String salt;

    //secret
    @Value("${token.secret}")
    private String secret;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private IUserLoginService iUserLoginService;

    @PostMapping("/login")
    public Result<?> userLogin(@RequestBody Map<String, String> paramsMap) {
        String username = paramsMap.get("username");
        String password = paramsMap.get("password");
        log.info("request in ,username:{},password:{}", username, password);
        //加密算法以及盐
        String hashPwd = BCrypt.hashpw((password), salt);
        //v1:未加密且直接从数据库验证,v2:update:加密切放入token
        return iUserLoginService.login(username, hashPwd);
    }

    @PostMapping("/registry")
    public Result<?> userRegistry(@RequestBody UserLogin user) {
        log.info("user:{}", user.toString());
        user.setPassword(BCrypt.hashpw(user.getPassword(), salt));
        boolean save = iUserLoginService.save(user);
        if (save) {
            return Result.OK();
        } else {
            return Result.error("注册失败");
        }
    }

    @PostMapping("/loginByShiro")
    public Result<?> userLoginByShiro(@RequestBody UserLogin user) {
        String password = user.getPassword();
        String username = user.getUsername();

        /* v1测试login使用，现正常走验证
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            //shiro登录方法，自定义realm处理身份验证
            subject.login(token);
            log.info("认证通过:{}",subject.isAuthenticated());
            //返回jwtToken
            String principal = (String) SecurityUtils.getSubject().getPrincipal();
            String jwtToken = JWTUtils.buildToken(principal, secret);
            redisTemplate.opsForValue().set(jwtToken,jwtToken);
            return Result.OK("登录成功",jwtToken);
        } catch (AuthenticationException e) {
            log.error("登录信息有误:{}",e.getMessage());
            return Result.error("登录信息有误");
        }*/
        UserLogin userLogin = iUserLoginService.getOne(
                new LambdaQueryWrapper<UserLogin>().
                        eq(UserLogin::getUsername, username));
        if (userLogin == null) {
            return Result.error("登录信息有误");
        }
        if (userLogin.getPassword() == null || "".equals(userLogin.getPassword().trim())) {
            return Result.error("密码不能为空!");
        }
        if (userLogin.getPassword().equals(getEncodePassword(password))) {
            String jwtToken = JWTUtils.buildToken(username, secret);
            redisTemplate.opsForValue().set(USER_TOKEN_PREFIX + jwtToken, jwtToken);
            redisTemplate.expire(USER_TOKEN_PREFIX + jwtToken, EXPIRE_TIME_DAY, TimeUnit.SECONDS);
            return Result.OK("登录成功", jwtToken);
        } else {
            return Result.error("登录信息有误");
        }
    }

    /**
     * 用户注册，shiro方式
     *
     * @param user 前端请求体user
     * @return
     */
    @PostMapping("/registryByShiro")
    public Result<?> registryBtShiro(@RequestBody UserLogin user) {
        if (!StringUtils.hasLength(user.getUsername()) || !StringUtils.hasLength(user.getPassword())) {
            if ("".equals(user.getUsername().trim()) || "".equals(user.getPassword().trim())) {
                return Result.error("用户名或密码不能为空!");
            }
        }
        user.setPassword(getEncodePassword(user.getPassword()));
        if (iUserLoginService.save(user)) {
            return Result.OK("注册成功");
        } else {
            return Result.error("注册失败,请重试");
        }
    }

    @RequestMapping("/unauth")
    @ResponseBody
    public Result<?> unAuth() {
        throw new AuthenticationException("token过期，请重新登录!");
    }

    //加密password
    public String getEncodePassword(String password) {
        //shiro对密码加密，密码作为盐
        ByteSource byteSource = ByteSource.Util.bytes(password);
        //生成密码
        return new SimpleHash("md5", password, byteSource, 2).toHex();
    }
}
