package com.zhidian.finalMq.config.global;

import com.zhidian.cloud.common.core.base.ApplicationConstant;
import com.zhidian.cloud.common.core.base.ApplicationContextHolder;
import com.zhidian.cloud.common.core.service.SendEmailService;
import com.zhidian.cloud.common.utils.common.IDLongKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 创建者:周广
 * 创建日期:2016/6/18 17:27
 * 文件简述:web配置文件。
 * 更新时间:
 */
@Configuration
public class MyWebAppConfiguration extends WebMvcConfigurerAdapter {


    //    //添加拦截器配置
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 多个拦截器组成一个拦截器链
//        // addPathPatterns 用于添加拦截规则
//        // excludePathPatterns 用户排除拦截
//        registry.addInterceptor(new TopInterceptor()).addPathPatterns("/**");
//        super.addInterceptors(registry);
//    }


//    @Bean//添加header处理
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", buildConfig());
//        return new CorsFilter(source);
//    }
//
//    private CorsConfiguration buildConfig() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*"); //允许任何域名使用
//        corsConfiguration.addAllowedHeader("*"); //允许任何头
//        corsConfiguration.addAllowedMethod("*"); //允许任何方法（post、get等）
//        return corsConfiguration;
//    }

    @Bean
    public IDLongKey idLongKey() {
        return new IDLongKey();
    }

    @Bean
    public SendEmailService sendEmailService() {
        return new SendEmailService();
    }


    @Bean
    public ApplicationConstant applicationConstant() {
        return new ApplicationConstant();
    }

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return ApplicationContextHolder.getInstance();
    }
}

