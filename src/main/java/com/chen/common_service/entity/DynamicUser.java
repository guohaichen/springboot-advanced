package com.chen.common_service.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class DynamicUser {
    private String id;
    private String name;
    private String gender;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String hobby;
}
