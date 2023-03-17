package com.chen.common_service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author cgh
 * @create 2022-06-22 9:11
 */
@Data
@TableName("outsource_resources")
public class OutSourceR {
    private java.lang.String id;
    private java.lang.String name;
    private java.lang.String gender;
    private java.lang.String telephone;
}
