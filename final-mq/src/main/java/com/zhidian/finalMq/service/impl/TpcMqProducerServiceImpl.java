package com.zhidian.finalMq.service.impl;

import com.paascloud.core.support.BaseService;
import com.zhidian.finalMq.mapper.TpcMqProducerMapper;
import com.zhidian.finalMq.model.domain.TpcMqProducer;
import com.zhidian.finalMq.model.vo.TpcMqProducerVo;
import com.zhidian.finalMq.model.vo.TpcMqPublishVo;
import com.zhidian.finalMq.service.TpcMqProducerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * The class Tpc mq producer service.
 *
 * @author paascloud.net @gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TpcMqProducerServiceImpl extends BaseService<TpcMqProducer> implements TpcMqProducerService {

	@Resource
	private TpcMqProducerMapper mdcMqProducerMapper;

	@Override
	public List<TpcMqProducerVo> listProducerVoWithPage(TpcMqProducer mdcMqProducer) {
		return mdcMqProducerMapper.listTpcMqProducerVoWithPage(mdcMqProducer);
	}

	@Override
	public List<TpcMqPublishVo> listPublishVoWithPage(TpcMqProducer mdcMqProducer) {
		return mdcMqProducerMapper.listTpcMqPublishVoWithPage(mdcMqProducer);
	}

	@Override
	public int deleteProducerById(Long producerId) {
		// 删除consumer
		mdcMqProducerMapper.deleteByPrimaryKey(producerId);
		// 删除发布关系
		return mdcMqProducerMapper.deletePublishByProducerId(producerId);
	}

	@Override
	public void updateOnLineStatusByPid(final String producerGroup) {
		logger.info("更新生产者pid={}状态为在线", producerGroup);
		this.updateStatus(producerGroup, 10);

	}

	@Override
	public void updateOffLineStatusByPid(final String producerGroup) {
		logger.info("更新生产者pid={}状态为离线", producerGroup);
		this.updateStatus(producerGroup, 20);
	}

	private void updateStatus(final String producerGroup, final int status) {
		TpcMqProducer tpcMqProducer = mdcMqProducerMapper.getByPid(producerGroup);
		if (tpcMqProducer.getStatus() != null && tpcMqProducer.getStatus() != status) {
			TpcMqProducer update = new TpcMqProducer();
			update.setStatus(status);
			update.setId(tpcMqProducer.getId());
			mdcMqProducerMapper.updateByPrimaryKeySelective(update);
		}
	}
}
