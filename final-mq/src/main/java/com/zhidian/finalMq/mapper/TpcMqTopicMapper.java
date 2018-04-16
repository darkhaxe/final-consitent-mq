package com.zhidian.finalMqmapper;

import com.paascloud.core.mybatis.MyMapper;
import com.zhidian.finalMqmodel.domain.TpcMqTopic;
import com.zhidian.finalMqmodel.vo.TpcMqTopicVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The interface Tpc mq topic mapper.
 *
 * @author paascloud.net @gmail.com
 */
@Mapper
@Component
public interface TpcMqTopicMapper extends MyMapper<TpcMqTopic> {
	/**
	 * List tpc mq topic vo with page list.
	 *
	 * @param tpcMqTopic the tpc mq topic
	 *
	 * @return the list
	 */
	List<TpcMqTopicVo> listTpcMqTopicVoWithPage(TpcMqTopic tpcMqTopic);
}