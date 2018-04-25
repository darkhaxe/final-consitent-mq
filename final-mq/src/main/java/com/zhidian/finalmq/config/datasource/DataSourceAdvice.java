package com.zhidian.finalmq.config.datasource;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.regex.Pattern;


/**
 * 针对指定的service方法切换主从库
 *
 * @author xianyongjie
 */
public class DataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice {

    /**
     * 匹配该正则表达式的方法名使用从库
     */
    private String useSlavePrefix;
    private Pattern p;

    @Override
    public void before(Method method, Object[] arg1, Object arg2)
            throws Throwable {
        if (p.matcher(method.getName()).matches()) {
            DataSourceSwitcher.setSlave();
        } else {
            DataSourceSwitcher.setMaster();
        }
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        DataSourceSwitcher.clearDataSource();
    }

    public String getUseSlavePrefix() {
        return useSlavePrefix;
    }

    public void setUseSlavePrefix(String useSlavePrefix) {
        this.useSlavePrefix = useSlavePrefix;
        p = Pattern.compile(this.useSlavePrefix);
    }

}
