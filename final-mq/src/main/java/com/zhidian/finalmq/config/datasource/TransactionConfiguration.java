package com.zhidian.finalmq.config.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 作者：周广
 * 创建时间：2016/12/10 0010
 * 必要描述:
 */
@Configuration
public class TransactionConfiguration {


    @Bean//注册事务管理器
    public DataSourceTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
        return new DynamicDataSourceTransactionManager(dynamicDataSource);
    }

}
