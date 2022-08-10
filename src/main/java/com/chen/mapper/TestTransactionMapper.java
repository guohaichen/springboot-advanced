package com.chen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.entity.DynamicUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cgh
 * @create 2022-06-22 16:09
 */
@Mapper
@DS("test")
public interface TestTransactionMapper extends BaseMapper<DynamicUser> {
}
