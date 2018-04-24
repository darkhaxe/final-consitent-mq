package com.zhidian.finalmq.config.datasource;

import com.zhidian.cloud.common.core.db.advice.DataSourceAdvice;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：周广
 * 创建时间：2016/12/10 0010
 * 必要描述:
 */
@Configuration
public class DataSourceConfiguration {

    private static final String QUERY = "SELECT 'x'";


    //主数据源 可读写
    private DataSource masterDataSource(MasterDataSourceConfigProp master) {
        DataSource masterSource = new DataSource();
        masterSource.setDriverClassName(master.getDriverClassName());
        masterSource.setInitSQL("SET NAMES 'utf8mb4'");
        masterSource.setUrl(master.getUrl());
        masterSource.setUsername(master.getUser());
        masterSource.setPassword(master.getPass());
        masterSource.setInitialSize(master.getInitialSize());
        masterSource.setMinIdle(master.getMinIdle());
        masterSource.setMaxIdle(master.getMaxIdle());
        masterSource.setMaxActive(master.getMaxActive());
        masterSource.setValidationQuery(QUERY);

        masterSource.setTestWhileIdle(true);
        masterSource.setTestOnBorrow(false);
        masterSource.setTestOnReturn(false);
        return masterSource;
    }

    //从数据源 只读
    private DataSource slaveDataSource(SlaveDataSourceConfigProp slave) {
        DataSource slaveSource = new DataSource();
        slaveSource.setInitSQL("SET NAMES 'utf8mb4'");
        slaveSource.setDriverClassName(slave.getDriverClassName());
        slaveSource.setUrl(slave.getUrl());
        slaveSource.setUsername(slave.getUser());
        slaveSource.setPassword(slave.getPass());
        slaveSource.setInitialSize(slave.getInitialSize());
        slaveSource.setMinIdle(slave.getMinIdle());
        slaveSource.setMaxIdle(slave.getMaxIdle());
        slaveSource.setMaxActive(slave.getMaxActive());
        slaveSource.setValidationQuery(QUERY);
        slaveSource.setTestWhileIdle(true);
        slaveSource.setTestOnBorrow(false);
        slaveSource.setTestOnReturn(false);
        return slaveSource;
    }


    @Bean(name = "baseDataSource")//注册动态数据源
    public DynamicDataSource dynamicDataSource(MasterDataSourceConfigProp master,
                                               SlaveDataSourceConfigProp slave) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource(master));
        Map<Object, Object> slaveSourceMap = new HashMap<>();
        slaveSourceMap.put("slave", slaveDataSource(slave));
        dynamicDataSource.setTargetDataSources(slaveSourceMap);
        return dynamicDataSource;
    }


    //数据源路由规则
    @Bean(name = "dataSourceAdvice")
    public DataSourceAdvice dataSourceAdvice() {
        DataSourceAdvice sourceAdvice = new DataSourceAdvice();
        sourceAdvice.setUseSlavePrefix("^(search|find|select|get|query|load|valid|list).*");
        return sourceAdvice;
    }

    //这里定义了一个通知，
    //它使用 dataSourceAdvice() 方法获取具体的执行规则
    //使用pointcut() 获取应用规则的办法
    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor() {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut());
        advisor.setAdvice(dataSourceAdvice());
        return advisor;
    }

    @Bean(name = "expPointcut")//注册一个切点
    public AspectJExpressionPointcut pointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.zhidian.*.service..*.*(..))");
        return pointcut;
    }


}
