package com.zhidian.finalmq.mapper;

import com.zhidian.finalmq.base.mybatis.MyMapper;
import com.zhidian.finalmq.model.domain.TpcJobTask;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * The interface Tpc job task mapper.
 *
 * @author paascloud.net @gmail.com
 */
@Mapper
@Component
public interface TpcJobTaskMapper extends MyMapper<TpcJobTask> {
}