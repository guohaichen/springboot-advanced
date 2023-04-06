package com.chen.common_service.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author cgh
 * @create 2023-03-30
 */
@Data
public class Photography {
    @TableId(value = "photography_id")
//    @TableField(value = "photography_id", fill = FieldFill.INSERT)
    private String photographyId;

    //创建人
    private String userId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private java.util.Date createTime;

    //存储路径
    private String imgUrl;

    //描述
    private String description;

    //相片标签
    private String tag;

    //
    private Integer stars;

    //
    private Integer collects;

}
