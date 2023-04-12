package com.chen.common_service.auth;

import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.UserLogin;
import com.chen.common_service.service.IUserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RequestMapping("/auth")
@RestController
public class LoginController {
    //从配置文件获取salt;
    @Value("${encode.salt}")
    private String salt;

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
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return Result.OK("登陆成功");
        } catch (AuthenticationException e) {
            log.error("登录信息有误:{}",e.getMessage());
            return Result.error("登录信息有误");
        }
    }

    /**
     * 用户注册，shiro方式
     * @param user 前端请求体user
     * @return
     */
    @PostMapping("/registryByShiro")
    public Result<?> registryBtShiro(@RequestBody UserLogin user) {
        log.info("user:{}", user.toString());
        //shiro密码加盐
//        ByteSource byteSource = ByteSource.Util.bytes(salt);
        //生成密码
//        SimpleHash password = new SimpleHash("md5", user.getPassword(), byteSource, 3);
        boolean save = iUserLoginService.save(user);
        if (save){
            return Result.OK("注册成功");
        }else {
            return Result.error("注册失败");
        }
    }

    @RequestMapping("/unauth")
    @ResponseBody
    public Result<?> unAuth() {
        return Result.noAuth("未认证");
    }
}
