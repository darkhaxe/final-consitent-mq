package com.zhidian.finalmq.config.datasource;

/**
 * 数据源选择类
 *
 * @author xianyongjie
 */
public class DataSourceSwitcher {
    @SuppressWarnings("rawtypes")
    private static final ThreadLocal contextHolder = new ThreadLocal();

    public static void setMaster() {
        clearDataSource();
    }

    public static void setSlave() {
        setDataSource("slave");
    }

    public static String getDataSource() {
        return (String) contextHolder.get();
    }

    @SuppressWarnings("unchecked")
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}
