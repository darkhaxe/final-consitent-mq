package com.zhidian.finalmq.mapper;

import com.zhidian.finalmq.base.mybatis.MyMapper;
import com.zhidian.finalmq.model.domain.TpcMqTag;
import com.zhidian.finalmq.model.vo.TpcMqTagVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The interface Tpc mq tag mapper.
 *
 * @author paascloud.net @gmail.com
 */
@Mapper
@Component
public interface TpcMqTagMapper extends MyMapper<TpcMqTag> {
	/**
	 * List tpc mq tag vo with page list.
	 *
	 * @param tpcMqTag the tpc mq tag
	 *
	 * @return the list
	 */
	List<TpcMqTagVo> listTpcMqTagVoWithPage(TpcMqTag tpcMqTag);
}