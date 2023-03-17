package com.chen.transcation;

import com.chen.common_service.entity.DynamicUser;
import com.chen.common_service.mapper.TestTransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cgh
 * @create 2022-06-22 15:56
 * Spring支持两种方式的事务管理
 * 1.编程式事务管理 TransactionTemplate/TransactionManager手动管理事务
 * 2.注解式@Transcation
 *
 * 推荐使用@Transaction 代码侵入性最小；
 * TransactionDefinition事务传播行为：当事务方法被另一个事务方法调用时，必须指定事务应该如何传播。
 */
@RestController
@RequestMapping("/trans")
public class TransactionDemo {
    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TestTransactionMapper testTransactionMapper;

    //编写一个需要回滚的事务，
    @GetMapping("test")
    public void trans() {
        //1.TransactionTemplate方式实现事务
        TransactionTemplate();
    }
    //方式二： transactionManager回滚
    private void TransactionManager(){
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            //业务代码
            transactionManager.commit(status);
        }catch (Exception e){
            //回滚
            transactionManager.rollback(status);
        }
    }

    private void TransactionTemplate() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    int i = 0;
                    i = testTransactionMapper.insert(new DynamicUser("004", "transaction", "1", "dec"));
                    //搞一个异常，让它爆错，执行不下去，然后回滚
                    int a = 5;
                    int b = a / 0;
                } catch (Exception e) {
                    //事务回滚
                    status.setRollbackOnly();
                }
            }
        });
    }
}
