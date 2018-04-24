package com.zhidian.finalmq.config.datasource;

import com.zhidian.cloud.common.core.db.advice.DataSourceAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;

/**
 * Created by liweitang on 2017/7/27.
 */
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

    private static Logger log = LoggerFactory.getLogger(DataSourceAdvice.class);

    public DynamicDataSourceTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        boolean isReadOnly = definition.isReadOnly();
        if (isReadOnly) {
            DataSourceSwitcher.setSlave();
            log.debug("只读事务，从库数据源名称：{}", DataSourceSwitcher.getDataSource());
        } else {
            DataSourceSwitcher.setMaster();
            log.debug("读写事务，主库数据源名称：{}", DataSourceSwitcher.getDataSource());
        }
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        DataSourceSwitcher.clearDataSource();
    }
}
