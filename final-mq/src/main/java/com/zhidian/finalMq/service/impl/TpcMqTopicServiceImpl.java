package com.zhidian.finalMq.service.impl;

import com.paascloud.core.support.BaseService;
import com.zhidian.finalMq.mapper.TpcMqTopicMapper;
import com.zhidian.finalMq.model.domain.TpcMqTopic;
import com.zhidian.finalMq.model.vo.TpcMqTopicVo;
import com.zhidian.finalMq.service.TpcMqTopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * The class Tpc mq topic service.
 *
 * @author paascloud.net @gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TpcMqTopicServiceImpl extends BaseService<TpcMqTopic> implements TpcMqTopicService {
	@Resource
	private TpcMqTopicMapper mdcMqTopicMapper;

	@Override
	public List<TpcMqTopicVo> listWithPage(TpcMqTopic mdcMqTopic) {
		return mdcMqTopicMapper.listTpcMqTopicVoWithPage(mdcMqTopic);
	}
}
