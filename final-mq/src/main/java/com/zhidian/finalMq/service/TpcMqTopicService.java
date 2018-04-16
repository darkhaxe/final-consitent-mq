package com.zhidian.finalMqservice;

import com.paascloud.core.support.IService;
import com.zhidian.finalMqmodel.domain.TpcMqTopic;
import com.zhidian.finalMqmodel.vo.TpcMqTopicVo;

import java.util.List;

/**
 * The interface Tpc mq topic service.
 *
 * @author paascloud.net @gmail.com
 */
public interface TpcMqTopicService extends IService<TpcMqTopic> {
	/**
	 * 查询MQ topic列表.
	 *
	 * @param mdcMqTopic the mdc mq topic
	 *
	 * @return the list
	 */
	List<TpcMqTopicVo> listWithPage(TpcMqTopic mdcMqTopic);

}
