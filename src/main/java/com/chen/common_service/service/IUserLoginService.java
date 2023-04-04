package com.chen.common_service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common_service.dto.Result;
import com.chen.common_service.entity.UserLogin;

/**
 * @author cgh
 * @create 2023-03-20
 */
public interface IUserLoginService extends IService<UserLogin> {
    Result<?> login(String username, String password);
}
