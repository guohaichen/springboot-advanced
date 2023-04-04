package com.chen.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author cgh
 * @create 2023-03-31
 * mybatis自动填充功能,1. 实现metaObjectHandler,实现方法
 * 2. 也可用通过实现Interceptor,重写intercept来实现
 * todo 整合字段自动填充类，目前仅测试用
 */
@Component
@Slf4j
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

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

//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
        //先写死，后面应该从shiro/spring security中获取，

        // 拦截插入和更新语句
//        if (invocation.getTarget() instanceof Executor) {
//            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
//            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
//            if (sqlCommandType == SqlCommandType.INSERT) {
//                Object parameter = invocation.getArgs()[1];
//                if (parameter instanceof Map) {
//                    Map<String, Object> paramMap = (Map<String, Object>) parameter;
//                    paramMap.put("user_id", )
//                }
//            }
//        }
//        return null;
//    }
}
