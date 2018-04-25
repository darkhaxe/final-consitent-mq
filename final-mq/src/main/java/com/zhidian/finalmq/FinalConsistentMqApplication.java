package com.zhidian.finalmq;

import com.zhidian.cloud.common.config.cache.CodisConfiguration;
import com.zhidian.cloud.common.config.cache.SessionRedisConfiguration;
import com.zhidian.cloud.common.config.cache.base.RedisBaseProperties;
import com.zhidian.cloud.common.config.mail.EmailConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author haze
 * @date created at 2018/4/24 上午11:23
 */
@SpringCloudApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@EnableCircuitBreaker
@EnableAsync
//MapperScan是在spring加载的时候就执行了扫描，所以无法从配置文件读取配置
@MapperScan(basePackages = {"com.zhidian.finalmq.mapper"})
@ComponentScan(basePackages = {
        "com.zhidian.finalmq.*"
//        "com.zhidian.finalmq.service",
//        "com.zhidian.finalmq.tpcapi.service.hystrix"
})
@Import({
        EmailConfiguration.class,
        RedisBaseProperties.class,
        SessionRedisConfiguration.class,
        CodisConfiguration.class,})
public class FinalConsistentMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinalConsistentMqApplication.class, args);
    }

}
