package com.chen.common_service.service.imlp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common_service.entity.UserLogin;
import com.chen.common_service.mapper.UserLoginMapper;
import com.chen.common_service.service.IUserLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author cgh
 * @create 2023-03-20
 */
@Service
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements IUserLoginService {

    @Resource
    private UserLoginMapper userLoginMapper;

    @Override
    public Boolean validatePassword(String username, String password) {
        String userPwd = userLoginMapper.selectOne(new LambdaQueryWrapper<UserLogin>().eq(UserLogin::getUserName, username)).getPassword();
        if (userPwd.equals(password)) {
            return true;
        } else {
            return false;
        }
    }
}
