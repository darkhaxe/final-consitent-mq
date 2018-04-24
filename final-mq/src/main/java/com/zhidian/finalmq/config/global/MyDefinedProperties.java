package com.zhidian.finalmq.config.global;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 作者：周广
 * 创建时间：2017/1/9 0009
 * 必要描述:
 */
@Order(1)
@Component
@ConfigurationProperties(
        prefix = "defined"
)
@Data
public class MyDefinedProperties {

    private String projectNamespace;
    private String projectDesc;
    private String controllerPath;

}
