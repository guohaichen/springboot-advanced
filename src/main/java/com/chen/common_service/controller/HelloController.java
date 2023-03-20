package com.chen.common_service.controller;

import com.chen.common_service.entity.DynamicUser;
import com.chen.common_service.mapper.DynamicUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/get")
    public String getName(@RequestHeader("Authorization") String token) {
        log.info("request's token from headers:{}", token);
        redisTemplate.opsForValue().set("token", token);
        return redisTemplate.opsForValue().get("token");
    }

    @GetMapping("/get/{name}")
    public String getNameByRedis(@PathVariable String name) {
        redisTemplate.opsForValue().set(name, name);
        return redisTemplate.opsForValue().get(name);
    }
}
