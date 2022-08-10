package com.chen.security.login.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author cgh
 * @create 2022-08-10 22:22
 * 用户实体类
 */
@Data
@TableName("user")
public class User {
    @TableId
    private java.lang.String id;

    private java.util.Date createTime;

    private java.util.Date updateTime;

    private java.lang.String updateBy;

    private java.lang.String createBy;

    private java.lang.String userName;

    private java.lang.String password;

    private java.lang.String nickName;
}
