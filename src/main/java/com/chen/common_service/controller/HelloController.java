package com.chen.common_service.controller;

import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.DynamicUser;
import com.chen.common_service.mapper.DynamicUserMapper;
import com.chen.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

import static com.chen.common_service.constant.UserConstant.USER_TOKEN_PREFIX;

/**
 * @author cgh
 * @create 2022-06-20 13:47
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/user")
public class HelloController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Value("${token.secret}")
    private  String secret;

    @Autowired
    private DynamicUserMapper dynamicUserMapper;

    @GetMapping("/list")
    public ArrayList<DynamicUser> getUser() {
        return dynamicUserMapper.getTestUser();
    }

    @GetMapping("/mapList")
    public Map<String, DynamicUser> getUserByMap() {
        return dynamicUserMapper.getTestUserByMap();
    }

    @GetMapping("/dev/list")
    public ArrayList<DynamicUser> getDevUser(HttpServletRequest request) {
        return dynamicUserMapper.getDevUser();
    }

    //授权，admin
//    @RequiresRoles("admin")
    @GetMapping("/get/token")
    public String getName(@RequestHeader("Authorization") String token) {
        log.info("request's token from headers:{}", token);
        Subject subject = SecurityUtils.getSubject();
        boolean authenticated = subject.isAuthenticated();
        log.info("authenticated:\t{}", authenticated);
        return redisTemplate.opsForValue().get(USER_TOKEN_PREFIX+token);
    }

    /**
     * 全局异常处理test
     * @return
     */
    @GetMapping("/exception")
    public Result<Integer> testException(){
        try {
            int a =  10 / 0;
        } catch (Exception e) {
            throw new ArithmeticException(e.getMessage());
        }
        return Result.OK("ok");
    }

    @GetMapping("/get/{name}")
    public String getNameByRedis(@PathVariable String name) {
        redisTemplate.opsForValue().set(name, name);
        return redisTemplate.opsForValue().get(name);
    }

    @GetMapping("/token/verify")
    public boolean verifyToken(@RequestHeader("Authorization")String token){
        try {
            JWTUtils.verifyToken(token, secret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
     return true;
    }
}
