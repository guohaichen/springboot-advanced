package com.chen.common_service.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.common_service.entity.DynamicUser;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author cgh
 * @create 2022-06-20 13:54
 */
@Mapper
public interface DynamicUserMapper extends BaseMapper<DynamicUser> {

    //ssm_dev数据库
    @DS("test")
    ArrayList<DynamicUser> getTestUser();

    //ssm_test数据库
    @DS("dev")
    ArrayList<DynamicUser> getDevUser();

    @DS("test")
    @MapKey("id")
    Map<String, DynamicUser> getTestUserByMap();
}
