package com.chen.common_service.entity;

import lombok.Data;

/**
 * @author cgh
 * @create 2023-08-02
 * 评论聚合后的映射pojo
 *
 */
@Data
public class CommentCount {
    private String id;
    //postId文章id
    private String postId;
    //该文章评论总数
    private Integer commentCount;

}
