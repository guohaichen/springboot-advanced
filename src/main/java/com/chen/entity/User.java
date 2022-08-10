package com.chen.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author cgh
 * @create 2022-06-20 13:48
 */
@Data
@AllArgsConstructor
@TableName("user_test")
public class User {
    private String id;
    private String name;
    private String gender;
    private String hobby;
}
