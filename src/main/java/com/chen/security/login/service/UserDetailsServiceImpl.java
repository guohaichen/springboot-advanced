package com.chen.security.login.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.security.login.entity.User;
import com.chen.security.login.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author cgh
 * @create 2022-08-10 15:44
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
        log.info("user:{}",user);
        return new LoginUser(user);
    }
}
