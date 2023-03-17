package com.chen.common_service.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.common_service.entity.OutSourceR;
import com.chen.common_service.mapper.DynamicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cgh
 * @create 2022-06-21 17:43
 * mybatis-动态sql一些demo
 */
@RestController
@RequestMapping("dynamic/user")
public class DynamicController {

    @Autowired
    private DynamicMapper dynamicMapper;

    //foreach 用法，比如我传一个集合，我想查询人员名字在这个集合中的人员
    @GetMapping("/list")
    public ArrayList<OutSourceR> getUserSupplier(){
        String[] array = {"李旭东","闫浩","胡杰"};

        ArrayList<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList,array);
        return dynamicMapper.getUserForeach(arrayList);
    }

    @GetMapping("all")
    public List<OutSourceR> getAllUser(){
        return dynamicMapper.selectList(new QueryWrapper<>());
    }

}
