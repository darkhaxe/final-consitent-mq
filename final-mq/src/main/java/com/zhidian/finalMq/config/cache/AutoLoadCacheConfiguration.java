package com.zhidian.finalMq..config.cache;

import com.zhidian.cloud.common.core.session.ShardedJedisClient;
import com.zhidian.cloud.common.utils.common.RedisPropertiesParse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：周广
 * 创建时间：2016/12/8 0008
 * 必要描述: 自动缓存框架配置
 */
@Configuration
public class AutoLoadCacheConfiguration {

    @Value("${data.ips}")
    private String redisProperties;
    @Value("${data.password}")
    private String myRedisPassword;
    @Value("${session.redis.ips}")
    private String sessionRedisProperties;
    @Value("${session.redis.password}")
    private String sessionRedisPassword;



    @Bean
    public ShardedJedisPool shardedJedisPool() {
        return new ShardedJedisPool(jedisPoolConfig(), jedisShardInfoList());
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(2000);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMinIdle(50);
        jedisPoolConfig.setMaxWaitMillis(2000);
        jedisPoolConfig.setTestOnBorrow(false);
        jedisPoolConfig.setTestOnReturn(false);
        jedisPoolConfig.setTestWhileIdle(false);
        return jedisPoolConfig;
    }

    //jedis客户端配置
    @Bean
    public List<JedisShardInfo> jedisShardInfoList() {
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>();
        RedisPropertiesParse propertiesParse = new RedisPropertiesParse(redisProperties);
        List<RedisPropertiesParse.MyRedisProperties> redisPropertiesList = propertiesParse.getRedisProperties();
        for (RedisPropertiesParse.MyRedisProperties properties : redisPropertiesList) {
            JedisShardInfo jedisShardInfo = new JedisShardInfo(properties.getIp(), properties.getPort());
            if (StringUtils.isNotBlank(myRedisPassword)) {
                jedisShardInfo.setPassword(myRedisPassword);
            }
            jedisShardInfoList.add(jedisShardInfo);
        }
        return jedisShardInfoList;
    }

    //Session专用缓存
    @Bean(name = "sessionCacheClient")
    public ShardedJedisClient shardedJedisClient() {
        return new ShardedJedisClient(sessionRedisProperties, jedisPoolConfig(), 1000, sessionRedisPassword);
    }


}
