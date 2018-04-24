package com.zhidian.finalmq.config.global;

import com.zhidian.cloud.common.utils.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 作者：周广
 * 创建时间：2016/12/9 0009
 * 必要描述:
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @Value("${appEnv}")
    private int appEnv;
    @Autowired
    private MyDefinedProperties myDefinedProperties;


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(myDefinedProperties.getControllerPath()))
                .paths(PathSelectors.any())
                .build()
                .genericModelSubstitutes(JsonResult.class)
                .useDefaultResponseMessages(false).enable(appEnv < 3);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(myDefinedProperties.getProjectDesc()).build();
    }
}
