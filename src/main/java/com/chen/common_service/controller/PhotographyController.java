package com.chen.common_service.controller;

import com.alibaba.fastjson.JSON;
import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.Photography;
import com.chen.common_service.mapper.PhotographyMapper;
import com.chen.common_service.service.IPhotographyService;
import com.chen.common_service.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cgh
 * @create 2023-03-30
 */
@RestController
@RequestMapping("/share")
@Slf4j
public class PhotographyController {

    @Autowired
    private PhotographyMapper photographyMapper;

    @Autowired
    private IPhotographyService iPhotographyService;

    @GetMapping("/photography")
    public Result<?> getPhotography(Photography photography) {
        //对image src的url 处理
        return iPhotographyService.listPhotography(photography);
    }

    @PostMapping("/photography")
    public Result<?> addPhotography(HttpServletRequest request, @RequestBody Photography photography) {
        String userInfoHeader = request.getHeader("UserInfo");
        UserInfo userInfo = JSON.parseObject(userInfoHeader, UserInfo.class);
        log.info("post request from header, userInfo:{}", userInfo.toString());
        //v1:硬编码保存编辑者的信息 后续通过MybatisMetaObjectHandler 整合shiro/spring-security 获取信息实现。
        photography.setUserId(userInfo.getId());
        int result = photographyMapper.insert(photography);
        if (result < 0) {
            return Result.error("插入失败");
        }
        return Result.OK("插入成功");
    }
}
