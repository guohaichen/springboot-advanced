package com.chen.common_service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.Photography;

/**
 * @author cgh
 * @create 2023-03-30
 */
public interface IPhotographyService extends IService<Photography> {
    Result<?> listPhotography(Photography photography);
}

