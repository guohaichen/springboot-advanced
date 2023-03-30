package com.chen.common_service.entity;

import lombok.Data;

/**
 * @author cgh
 * @create 2023-03-30
 */
@Data
public class Photography {
    private String photographyId;

    //创建人
    private String userId;

    private java.util.Date createTime;

    //存储路径
    private String src;

    //描述
    private String description;

    //相片标签
    private String tag;

    //
    private Integer stars;

    //
    private Integer collects;

}
