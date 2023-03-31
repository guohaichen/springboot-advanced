package com.chen.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author cgh
 * @create 2023-03-31
 * mybatis自动填充功能,实现metaObjectHandler,实现方法
 */
@Component
@Slf4j
public class MybatisMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert ....");
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        //主键自动填充待优化
        this.strictInsertFill(metaObject, "photographyId", String.class, UUID.randomUUID().toString());
    }
    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
