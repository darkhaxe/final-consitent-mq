package com.zhidian.finalmq.config.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 作者：周广
 * 创建时间：2016/12/10 0010
 * 必要描述:
 */
@Component
@ConfigurationProperties(
        prefix = "masterDataSource"
)
@Data
public class MasterDataSourceConfigProp {

    private String url;
    private String user;
    private String pass;
    private String driverClassName;


    private int initialSize;
    private int minIdle;
    private int maxIdle;
    private int maxActive;


}
