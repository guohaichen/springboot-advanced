package com.chen.common_service.service.imlp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.UserLogin;
import com.chen.common_service.mapper.UserLoginMapper;
import com.chen.common_service.service.IUserLoginService;
import com.chen.common_service.vo.UserInfo;
import com.chen.utils.JWTUtils;
import com.chen.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author cgh
 * @create 2023-03-20
 */
@Service
@Slf4j
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements IUserLoginService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private UserLoginMapper userLoginMapper;

    @Value("${token.secret}")
    private String secret;

    @Override
    public Result<?> login(String username, String password) {
        UserLogin userLogin = userLoginMapper.selectOne(new LambdaQueryWrapper<UserLogin>().eq(UserLogin::getUsername, username));
        if (userLogin == null) {
            return Result.error("用户不存在");
        }
        String userPwd = userLogin.getPassword();
        if (userPwd.equals(password)) {
            //v1模拟一个token,先返回
            //String token = UUID.randomUUID().toString().replace("-","").substring(0,10)+"-"+new Date().getTime();
            //v2.使用了jwt生成token，并将token放入redis中
            TokenUtils tokenUtils = new TokenUtils(redisTemplate);
            String token = JWTUtils.buildToken(username,secret);
            tokenUtils.putTokenWithExpiration(token);
            //update-- 给客户端返回用户的基本信息
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(userLogin, userInfo);
            userInfo.setToken(token);
            //用户信息存入redis一份（未加密）
            putUserInfoInRedis(userLogin.getId(),userInfo);
            return Result.OK("验证成功",userInfo);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 用户信息存入redis一份
     * @param key id
     * @param userInfo 用户信息
     */
    public void putUserInfoInRedis(String key, UserInfo userInfo){
        String jsonString = JSON.toJSONString(userInfo);
        Map map = JSON.parseObject(jsonString, Map.class);
        log.info("user map:{}",map.toString());
        redisTemplate.opsForHash().putAll(key,map);
    }
}
