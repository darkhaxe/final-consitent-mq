package com.zhidian.finalmq.mapper;

import com.zhidian.finalmq.base.mybatis.MyMapper;
import com.zhidian.finalmq.model.domain.TpcMqSubscribeTag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * The interface Tpc mq subscribe tag mapper.
 *
 * @author paascloud.net @gmail.com
 */
@Mapper
@Component
public interface TpcMqSubscribeTagMapper extends MyMapper<TpcMqSubscribeTag> {
}