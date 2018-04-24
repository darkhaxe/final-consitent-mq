package com.zhidian.finalmq.config.datasource;

import com.zhidian.cloud.common.core.db.advice.DataSourceSwitcher;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态切换数据源
 *
 * @author xianyongjie
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceSwitcher.getDataSource();
    }

}
