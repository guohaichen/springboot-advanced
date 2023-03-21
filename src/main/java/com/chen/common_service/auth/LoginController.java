package com.chen.common_service.auth;

import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.UserLogin;
import com.chen.common_service.service.IUserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        //v1:未加密且直接从数据库验证
        return iUserLoginService.validatePassword(username, hashPwd);
    }

    @PostMapping("/registry")
    public Result<?> userRegistry(@RequestBody UserLogin user){
        log.info("user:{}",user.toString());
        user.setPassword(BCrypt.hashpw(user.getPassword(),salt));
        boolean save = iUserLoginService.save(user);
        if (save){
            return Result.OK();
        }else {
            return Result.error("注册失败");
        }
    }
}
