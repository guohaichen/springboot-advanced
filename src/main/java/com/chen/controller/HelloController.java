package com.chen.controller;

import com.chen.entity.User;
import com.chen.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

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
    private UserMapper userMapper;

    @GetMapping("list")
    public ArrayList<User> getUser(){
        return userMapper.getTestUser();
    }


    @GetMapping("dev/list")
    public ArrayList<User> getDevUser(){
        return userMapper.getDevUser();
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
