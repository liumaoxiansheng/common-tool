package com.example.commontool.utils.spring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

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
