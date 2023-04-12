package com.chen.common_service.auth;

import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.UserLogin;
import com.chen.common_service.service.IUserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
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

    @PostMapping("/loginByShiro")
    public Result<?> userLoginByShiro(@RequestBody UserLogin userLogin){
    new UsernamePasswordToken()

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

    /**
     * 用户注册，shiro方式
     * @param user 前端请求体user
     * @return
     */
    @PostMapping("/registryByShiro")
    public Result<?> registryBtShiro(@RequestBody UserLogin user){
        log.info("user:{}",user.toString());
        //shiro密码加盐
        ByteSource byteSource = ByteSource.Util.bytes(salt);
        //生成密码
        SimpleHash md5 = new SimpleHash("md5", user, byteSource, 3);
    }

    @RequestMapping("/unauth")
    @ResponseBody
    public Result<?> unAuth(){
        return Result.noAuth("未认证");
    }
}
