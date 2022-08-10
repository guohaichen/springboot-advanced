package com.chen.security.login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.security.login.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cgh
 * @create 2022-08-10 22:28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
