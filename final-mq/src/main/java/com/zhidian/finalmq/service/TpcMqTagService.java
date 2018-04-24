package com.zhidian.finalmq.service;

import com.zhidian.finalmq.base.IService;
import com.zhidian.finalmq.model.domain.TpcMqTag;
import com.zhidian.finalmq.model.vo.TpcMqTagVo;

import java.util.List;

/**
 * The interface Tpc mq tag service.
 *
 * @author paascloud.net @gmail.com
 */
public interface TpcMqTagService extends IService<TpcMqTag> {
	/**
	 * 查询Tag列表.
	 *
	 * @param mdcMqTag the mdc mq tag
	 *
	 * @return the list
	 */
	List<TpcMqTagVo> listWithPage(TpcMqTag mdcMqTag);

	/**
	 * 根据ID删除TAG.
	 *
	 * @param tagId the tag id
	 *
	 * @return the int
	 */
	int deleteTagById(Long tagId);
}
