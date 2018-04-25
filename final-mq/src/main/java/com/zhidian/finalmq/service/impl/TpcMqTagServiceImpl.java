package com.zhidian.finalmq.service.impl;

import com.github.pagehelper.PageHelper;
import com.zhidian.finalmq.mapper.TpcMqTagMapper;
import com.zhidian.finalmq.model.domain.TpcMqTag;
import com.zhidian.finalmq.model.vo.TpcMqTagVo;
import com.zhidian.finalmq.service.BaseService;
import com.zhidian.finalmq.service.TpcMqConsumerService;
import com.zhidian.finalmq.service.TpcMqTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * The class Tpc mq tag service.
 *
 * @author paascloud.net @gmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TpcMqTagServiceImpl extends BaseService<TpcMqTag> implements TpcMqTagService {

    @Resource
    private TpcMqTagMapper mdcMqTagMapper;
    @Resource
    private TpcMqConsumerService mdcMqConsumerService;

    @Override
    public List<TpcMqTagVo> listWithPage(TpcMqTag tpcMqTag) {
        Integer pageNum = tpcMqTag.getPageNum();
        Integer pageSize = tpcMqTag.getPageSize();
        PageHelper.startPage(pageNum == null ? 0 : pageNum, pageSize == null ? 0 : pageSize);
        tpcMqTag.setOrderBy("update_time desc");
        return mdcMqTagMapper.listTpcMqTagVoWithPage(tpcMqTag);
    }

    @Override
    public int deleteTagById(Long tagId) {
        // 删除订阅的tag
        mdcMqConsumerService.deleteSubscribeTagByTagId(tagId);
        // 删除tag
        return mdcMqTagMapper.deleteByPrimaryKey(tagId);
    }
}
