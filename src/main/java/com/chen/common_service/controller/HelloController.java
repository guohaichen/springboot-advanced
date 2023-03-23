package com.chen.common_service.controller;

import com.chen.common_service.entity.DynamicUser;
import com.chen.common_service.mapper.DynamicUserMapper;
import com.chen.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author cgh
 * @create 2022-06-20 13:47
 */
@RestController
@Slf4j
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


    @GetMapping("/get/token")
    public String getName(@RequestHeader("Authorization") String token) {
        log.info("request's token from headers:{}", token);
        return redisTemplate.opsForValue().get(token);
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
