package com.chen.common_service.service.imlp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.UserLogin;
import com.chen.common_service.mapper.UserLoginMapper;
import com.chen.common_service.service.IUserLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @author cgh
 * @create 2023-03-20
 */
@Service
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements IUserLoginService {

    @Resource
    private UserLoginMapper userLoginMapper;

    @Override
    public Result<?> validatePassword(String username, String password) {
        UserLogin userLogin = userLoginMapper.selectOne(new LambdaQueryWrapper<UserLogin>().eq(UserLogin::getUsername, username));
        if (userLogin == null) {
            return Result.error("用户不存在");
        }
        String userPwd = userLogin.getPassword();
        if (userPwd.equals(password)) {
            //模拟一个token,先返回
            String token = UUID.randomUUID().toString().replace("-","").substring(0,10)+"-"+new Date().getTime();
            return Result.OK(token);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
}
