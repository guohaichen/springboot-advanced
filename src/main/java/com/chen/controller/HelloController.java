package com.chen.controller;

import com.chen.entity.DynamicUser;
import com.chen.mapper.DynamicUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author cgh
 * @create 2022-06-20 13:47
 */
@RestController
@RequestMapping("user")
public class HelloController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    private DynamicUserMapper dynamicUserMapper;

    @GetMapping("list")
    public ArrayList<DynamicUser> getUser(){
        return dynamicUserMapper.getTestUser();
    }

    @GetMapping("mapList")
    public Map<String,DynamicUser> getUserByMap(){
        return dynamicUserMapper.getTestUserByMap();
    }

    @GetMapping("dev/list")
    public ArrayList<DynamicUser> getDevUser(HttpServletRequest request){
        return dynamicUserMapper.getDevUser();
    }


    @GetMapping("/get")
    public String getName(){
        redisTemplate.opsForValue().set("name","tom");
        return redisTemplate.opsForValue().get("name");
    }

    @GetMapping("/get/{name}")
    public String getName(@PathVariable String name){
        redisTemplate.opsForValue().set(name,name);
        return redisTemplate.opsForValue().get(name);
    }
}
