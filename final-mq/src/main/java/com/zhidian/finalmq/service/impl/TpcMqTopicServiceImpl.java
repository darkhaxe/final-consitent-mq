package com.zhidian.finalmq.service.impl;

import com.zhidian.finalmq.mapper.TpcMqTopicMapper;
import com.zhidian.finalmq.model.domain.TpcMqTopic;
import com.zhidian.finalmq.model.vo.TpcMqTopicVo;
import com.zhidian.finalmq.service.BaseService;
import com.zhidian.finalmq.service.TpcMqTopicService;
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
