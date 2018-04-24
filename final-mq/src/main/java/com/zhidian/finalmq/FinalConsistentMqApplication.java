package com.zhidian.finalmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author haze
 * @date created at 2018/4/24 上午11:23
 */
@EnableCaching
@EnableHystrix
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@EnableTransactionManagement
public class FinalConsistentMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinalConsistentMqApplication.class, args);
    }

}
