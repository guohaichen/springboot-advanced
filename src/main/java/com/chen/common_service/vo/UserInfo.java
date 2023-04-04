package com.chen.common_service.vo;

import lombok.Data;

/**
 * 返回用户信息对象
 */
@Data
public class UserInfo {
    private String id;

    private String username;

    private String nickname;

    private String phone;

    private String token;
}
