package com.zhidian.finalMq.mapper;

import com.paascloud.core.mybatis.MyMapper;
import com.zhidian.finalMq.model.domain.TpcMqTag;
import com.zhidian.finalMq.model.vo.TpcMqTagVo;
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