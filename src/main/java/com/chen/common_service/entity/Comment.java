package com.chen.common_service.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author cgh
 * @create 2023-08-01
 * 评论实体类
 */
@Data
public class Comment {
    private String id;
    //评论
    private String commentMsg;
    //评论归属
    private String postId;
    //关联回复主键
    private String replayId;

    private Integer likesCount;
    private Integer dislikesCount;
    private Date createTime;

    public Comment(String commentMsg, String postId, String replayId, Integer likesCount, Integer dislikesCount, Date createTime) {
        this.commentMsg = commentMsg;
        this.postId = postId;
        this.replayId = replayId;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.createTime = createTime;
    }
}
