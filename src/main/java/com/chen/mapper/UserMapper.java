package com.chen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

/**
 * @author cgh
 * @create 2022-06-20 13:54
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @DS("test")
    ArrayList<User> getTestUser();

    @DS("dev")
    ArrayList<User> getDevUser();
}
