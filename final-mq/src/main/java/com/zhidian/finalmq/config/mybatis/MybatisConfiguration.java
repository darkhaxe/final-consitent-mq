package com.zhidian.finalmq.config.mybatis;

import com.github.pagehelper.PageHelper;
import com.zhidian.finalmq.config.datasource.DynamicDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * 作者：周广
 * 创建时间：2017/1/9 0009
 * 必要描述:
 */
@Configuration
public class MybatisConfiguration {


    @Bean//手动注册一个SqlSessionFactory
    public SqlSessionFactoryBean sqlSessionFactoryBean(DynamicDataSource dataSource,
                                                       MybatisProperties properties) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(properties.getTypeAliasesPackage());

        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        if (properties.getMapperLocations() != null && properties.getMapperLocations().length > 0) {
            List<Resource> resources = new ArrayList<>();
            for (int i = 0; i < properties.getMapperLocations().length; i++) {
                try {
                    Collections.addAll(resources, resourceResolver.getResources(properties.getMapperLocations()[i]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Resource[] resourcesArr = new Resource[resources.size()];
            bean.setMapperLocations(resources.toArray(resourcesArr));
        }
        //PageHelper插件注册到mybatis拦截器，以允许PageHelper.startPage(1,1);分页
        Interceptor[] plugins = new Interceptor[]{pageHelper()};
        bean.setPlugins(plugins);
        return bean;
    }

    //注册mybatis分页插件PageHelper
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "false");
        p.setProperty("dialect", "mysql");
        pageHelper.setProperties(p);
        return pageHelper;
    }

}
