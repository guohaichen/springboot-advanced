package com.chen.common_service.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.common_service.entity.OutSourceR;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

/**
 * @author cgh
 * @create 2022-06-21 17:45
 */
@Mapper
public interface DynamicMapper extends BaseMapper<OutSourceR> {

    @DS("dev")
    ArrayList<OutSourceR> getUserForeach(ArrayList<String> arrayList);
}
