package com.zhidian.finalmq.service;

import com.zhidian.finalmq.base.IService;
import com.zhidian.finalmq.model.domain.TpcMqProducer;
import com.zhidian.finalmq.model.vo.TpcMqProducerVo;
import com.zhidian.finalmq.model.vo.TpcMqPublishVo;

import java.util.List;

/**
 * The interface Tpc mq producer service.
 *
 * @author paascloud.net @gmail.com
 */
public interface TpcMqProducerService extends IService<TpcMqProducer> {
	/**
	 * List producer vo with page list.
	 *
	 * @param mdcMqProducer the mdc mq producer
	 *
	 * @return the list
	 */
	List<TpcMqProducerVo> listProducerVoWithPage(TpcMqProducer mdcMqProducer);

	/**
	 * 查询发布者列表.
	 *
	 * @param mdcMqProducer the mdc mq producer
	 *
	 * @return the list
	 */
	List<TpcMqPublishVo> listPublishVoWithPage(TpcMqProducer mdcMqProducer);

	/**
	 * 根据生产者ID删除生产者.
	 *
	 * @param id the id
	 *
	 * @return the int
	 */
	int deleteProducerById(Long id);

	/**
	 * 根据pid更新生产者状态为在线.
	 *
	 * @param producerGroup the producer group
	 */
	void updateOnLineStatusByPid(String producerGroup);

	/**
	 * 根据pid更新生产者状态为离线.
	 *
	 * @param producerGroup the producer group
	 */
	void updateOffLineStatusByPid(String producerGroup);
}
