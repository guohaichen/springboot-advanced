package com.chen.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author cgh
 * @create 2023-03-23
 */
public class TokenUtils {

    private final StringRedisTemplate redisTemplate;

    public TokenUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void putToken(String token) {
        redisTemplate.opsForValue().set(token,token);
    }
}
