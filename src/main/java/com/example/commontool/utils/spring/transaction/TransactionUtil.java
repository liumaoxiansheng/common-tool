package com.example.commontool.utils.spring.transaction;

import com.example.commontool.testservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;

/**
 * @ClassName: TransactionUtil
 * @Description: 编程式手动事务工具
 * @Author: th_legend
 **/
@Component
public class TransactionUtil {

    @Autowired
    private DataSourceTransactionManager transactionManager;

    /**
     * 开启事务
     *
     * @return:
     * @Author: th_legend
     **/
    public TransactionStatus begin() {
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());

        return transaction;
    }

    public void testTransaction(){
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        // 开启事务
        TransactionStatus transactionStatus = begin(transactionDefinition);
        try {
            // 执行sql
            // 注册事务状态监听
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public int getOrder() {
                    return TransactionSynchronization.super.getOrder();
                }

                @Override
                public void suspend() {
                    System.out.println("事务挂起...");
                }

                @Override
                public void resume() {
                    System.out.println("事务恢复...");
                }

                @Override
                public void flush() {
                    TransactionSynchronization.super.flush();
                }

                @Override
                public void beforeCommit(boolean readOnly) {
                    System.out.println("事务准备提交...");
                }

                @Override
                public void beforeCompletion() {
                    System.out.println("事务准备提交或回滚...");
                }

                @Override
                public void afterCommit() {
                    System.out.println("事务提交完成...");
                }

                @Override
                public void afterCompletion(int status) {
                    System.out.println("事务提交活活滚完成...");
                }
            });
           // xxx();
           commit(transactionStatus);
        }catch (Exception e){
            rollback(transactionStatus);
        }

    }

    /**
     * 开启事务
     *
     * @return:
     * @Author: th_legend
     **/
    public TransactionStatus begin(TransactionDefinition transactionDefinition) {
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        return transaction;
    }

    /**
     * 提交事务
     *
     * @return:
     * @Author: th_legend
     **/
    public void commit(TransactionStatus transaction) {
        if (null != transaction) {
            transactionManager.commit(transaction);
        }
    }

    /**
     * 回滚事务
     *
     * @return:
     * @Author: th_legend
     **/
    public void rollback(TransactionStatus transaction) {
        if (null != transaction) {
            transactionManager.rollback(transaction);
        }
    }

    /**
     * 获取数据源对象
     *
     * @return:
     * @Author: th_legend
     **/
    public DataSource getDataSource(){
      return   transactionManager.getDataSource();
    }
}
