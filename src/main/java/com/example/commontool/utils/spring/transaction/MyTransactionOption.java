package com.example.commontool.utils.spring.transaction;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;

/**
 * @ClassName: MyTransactionOption
 * @Description: 事务操作,结合org.springframework.transaction.support.TransactionSynchronizationManager工具使用
 * @Author: th_legend
 **/
public class MyTransactionOption extends TransactionSynchronizationAdapter {
    @Override
    public void beforeCommit(boolean readOnly) {
        System.out.println("MyTransactionOption.beforeCommit().....");
    }


    @Override
    public void afterCommit() {
        System.out.println("MyTransactionOption.afterCommit().....");
    }


}
