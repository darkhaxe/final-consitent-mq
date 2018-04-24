package com.zhidian.finalmq.service.impl;

import com.google.common.base.Preconditions;
import com.zhidian.finalmq.base.dto.MessageQueryDto;
import com.zhidian.finalmq.base.util.PublicUtil;
import com.zhidian.finalmq.base.wrapper.Wrapper;
import com.zhidian.finalmq.enums.ErrorCodeEnum;
import com.zhidian.finalmq.exceptions.TpcBizException;
import com.zhidian.finalmq.mapper.TpcMqConfirmMapper;
import com.zhidian.finalmq.mapper.TpcMqMessageMapper;
import com.zhidian.finalmq.model.domain.TpcMqConfirm;
import com.zhidian.finalmq.model.domain.TpcMqMessage;
import com.zhidian.finalmq.model.dto.MessageTaskQueryDto;
import com.zhidian.finalmq.model.dto.TpcMqMessageDto;
import com.zhidian.finalmq.model.enums.MqSendStatusEnum;
import com.zhidian.finalmq.model.vo.TpcMessageVo;
import com.zhidian.finalmq.mq.RocketMqProducer;
import com.zhidian.finalmq.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class Tpc mq message service.
 *
 * @author paascloud.net @gmail.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Throwable.class)
public class TpcMqMessageServiceImpl extends BaseService<TpcMqMessage> implements TpcMqMessageService {
    @Resource
    private TpcMqMessageMapper tpcMqMessageMapper;
    @Resource
    private TpcMqConfirmMapper tpcMqConfirmMapper;
    @Resource
    private TpcMqConsumerService tpcMqConsumerService;
//    @Resource
//    private UacRpcService uacRpcService;
//    @Resource
//    private MdcRpcService mdcRpcService;
//    @Resource
//    private OpcRpcService opcRpcService;

    /**
     * 仅入库
     */
    @Override
    public void saveMessageWaitingConfirm(TpcMqMessageDto messageDto) {

        if (StringUtils.isEmpty(messageDto.getMessageTopic())) {
            throw new TpcBizException(ErrorCodeEnum.TPC10050001);
        }

        Date now = new Date();
        TpcMqMessage message = new ModelMapper().map(messageDto, TpcMqMessage.class);
        message.setMessageStatus(MqSendStatusEnum.WAIT_SEND.sendStatus());
        message.setUpdateTime(now);
        message.setCreatedTime(now);
        tpcMqMessageMapper.insertSelective(message);
    }

    /**
     * 入库并发送mq
     *
     * @param messageKey the message key
     */
    @Override
    public void confirmAndSendMessage(String messageKey) {
        final TpcMqMessage message = tpcMqMessageMapper.getByMessageKey(messageKey);
        if (message == null) {
            throw new TpcBizException(ErrorCodeEnum.TPC10050002);
        }

        TpcMqMessage update = new TpcMqMessage();
        update.setMessageStatus(MqSendStatusEnum.SENDING.sendStatus());
        update.setId(message.getId());
        update.setUpdateTime(new Date());
        tpcMqMessageMapper.updateByPrimaryKeySelective(update);
        // 创建消费待确认列表
        this.createMqConfirmListByTopic(message.getMessageTopic(), message.getId(), message.getMessageKey());
        // 直接发送消息
        this.directSendMessage(message.getMessageBody(),
                message.getMessageTopic(),
                message.getMessageTag(),
                message.getMessageKey(),
                message.getProducerGroup(),
                message.getDelayLevel());
    }

    /**
     * 不入库,直接发送到mq
     */
    @Override
    public void directSendMessage(String body, String topic, String tag, String key, String pid, Integer delayLevel) {
        RocketMqProducer.sendSimpleMessage(body, topic, tag, key, pid, delayLevel);
    }

    @Override
    public void saveAndSendMessage(TpcMqMessageDto dto) {
        if (StringUtils.isEmpty(dto.getMessageTopic())) {
            throw new TpcBizException(ErrorCodeEnum.TPC10050001);
        }

        Date now = new Date();
        TpcMqMessage message = new ModelMapper().map(dto, TpcMqMessage.class);
        message.setMessageStatus(MqSendStatusEnum.SENDING.sendStatus());
        message.setId(generateId());
        message.setUpdateTime(now);
        message.setCreatedTime(now);

        tpcMqMessageMapper.insertSelective(message);
        // 创建消费待确认列表
        this.createMqConfirmListByTopic(message.getMessageTopic(), message.getId(), message.getMessageKey());
        this.directSendMessage(dto.getMessageBody(),
                dto.getMessageTopic(),
                dto.getMessageTag(),
                dto.getMessageKey(),
                dto.getProducerGroup(),
                dto.getDelayLevel());
    }

    @Override
    public void resendMessageByMessageId(Long messageId) {
        final TpcMqMessage message = tpcMqMessageMapper.selectByPrimaryKey(messageId);
        if (message == null) {
            throw new TpcBizException(ErrorCodeEnum.TPC10050006);
        }
        this.resendMessage(message);
    }

    @Override
    public void resendMessageByMessageKey(String messageKey) {
        final TpcMqMessage task = tpcMqMessageMapper.getByMessageKey(messageKey);
        this.resendMessage(task);
    }

    @Override
    public void setMessageToAlreadyDead(Long messageId) {
        final TpcMqMessage task = tpcMqMessageMapper.selectByPrimaryKey(messageId);
        if (task == null) {
            throw new TpcBizException(ErrorCodeEnum.TPC10050006);
        }

        tpcMqMessageMapper.updateAlreadyDeadByMessageId(messageId);
    }

    @Override
    public void deleteMessageByMessageKey(String messageKey) {

        int result = tpcMqMessageMapper.deleteMessageByMessageKey(messageKey);
        if (result < 1) {
            throw new TpcBizException(ErrorCodeEnum.TPC10050003, messageKey);
        }
    }

    @Override
    public void resendAllDeadMessageByTopicName(String topicName, int batchSize) {
        // 1.查询该topic下所有死亡的消息
        // 2.分页
    }

    @Override
    public List<TpcMqMessage> listMessageForWaitingProcess(MessageTaskQueryDto query) {
        return tpcMqMessageMapper.listMessageForWaitingProcess(query);
    }

    @Override
    public void confirmReceiveMessage(final String cid, final String messageKey) {
        // 1. 校验cid
        // 2. 校验messageKey
        // 3. 校验cid 和 messageKey
        Long confirmId = tpcMqConfirmMapper.getIdMqConfirm(cid, messageKey);
        // 3. 更新消费信息的状态
        tpcMqConfirmMapper.confirmReceiveMessage(confirmId);
    }

    @Override
    public void confirmConsumedMessage(final String cid, final String messageKey) {
        Long confirmId = tpcMqConfirmMapper.getIdMqConfirm(cid, messageKey);
        tpcMqConfirmMapper.confirmConsumedMessage(confirmId);
    }

    @Override
    public int updateMqConfirmStatus(final String cid, final String messageKey) {
        return 0;
    }

    @Override
    public void createMqConfirmListByTopic(final String topic, final Long messageId, final String messageKey) {
        List<String> consumerGroupList = tpcMqConsumerService.listConsumerGroupByTopic(topic);
        if (PublicUtil.isEmpty(consumerGroupList)) {
            throw new TpcBizException(ErrorCodeEnum.TPC100500010, topic);
        }

        tpcMqConfirmMapper.batchCreateMqConfirm(
                consumerGroupList.stream()
                        .map((String cid) ->
                                //todo
                                new TpcMqConfirm(0L, messageId, messageKey, cid))
//                                new TpcMqConfirm(UniqueIdGenerator.generateId(), messageId, messageKey, cid))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<String> queryWaitingConfirmMessageKeyList(final MessageTaskQueryDto query) {
        return tpcMqMessageMapper.queryWaitingConfirmMessageKeyList(query);
    }

    @Override
    public void handleWaitingConfirmMessage(final List<String> deleteKeyList, final List<String> resendKeyList) {
        tpcMqMessageMapper.batchDeleteMessage(deleteKeyList);
        for (final String messageKey : resendKeyList) {
            this.confirmAndSendMessage(messageKey);
        }
    }

    @Override
    public int updateMqMessageTaskStatus(final TpcMqMessage message) {
        return tpcMqMessageMapper.updateMqMessageTaskStatus(message);
    }

    @Override
    public int updateMqMessageStatus(final TpcMqMessage update) {
        return tpcMqMessageMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    public Wrapper queryRecordListWithPage(final MessageQueryDto messageQueryDto) {
        String producerGroup = messageQueryDto.getProducerGroup();
        String messageKey = messageQueryDto.getMessageKey();
        Preconditions.checkArgument(StringUtils.isNotEmpty(producerGroup) || StringUtils.isNotEmpty(messageKey), "messageKey 和 pid 必须选择一个");
        if (StringUtils.isEmpty(producerGroup)) {
//            List<MqMessageVo> result = Lists.newArrayList();
//            Wrapper<PageInfo<MqMessageVo>> uacWrapper = uacRpcService.queryMessageListWithPage(messageQueryDto);
//            Wrapper<PageInfo<MqMessageVo>> mdcWrapper = mdcRpcService.queryMessageListWithPage(messageQueryDto);
//            Wrapper<PageInfo<MqMessageVo>> opcWrapper = opcRpcService.queryMessageListWithPage(messageQueryDto);

//            if (uacWrapper != null && uacWrapper.getResult() != null) {
//                List<MqMessageVo> list = uacWrapper.getResult().getList();
//                result.addAll(list);
//            }
//            if (mdcWrapper != null && mdcWrapper.getResult() != null) {
//                List<MqMessageVo> list = mdcWrapper.getResult().getList();
//                result.addAll(list);
//            }
//            if (opcWrapper != null && opcWrapper.getResult() != null) {
//                List<MqMessageVo> list = opcWrapper.getResult().getList();
//                result.addAll(list);
//            }
//            return WrapMapper.ok(new PageInfo<>(result));
//        }
//        if (StringUtils.equals(PIDEnum.PID_UAC.name(), producerGroup)) {
//            return uacRpcService.queryMessageListWithPage(messageQueryDto);
//        } else if (StringUtils.equals(PIDEnum.PID_MDC.name(), producerGroup)) {
//            return mdcRpcService.queryMessageListWithPage(messageQueryDto);
//        } else if (StringUtils.equals(PIDEnum.PID_OPC.name(), producerGroup)) {
//            return opcRpcService.queryMessageListWithPage(messageQueryDto);
//        } else {
//            log.error("pid没有维护 pid={}", producerGroup);
        }
        return null;
    }

    @Override
    public List<TpcMessageVo> listReliableMessageVo(final MessageQueryDto messageQueryDto) {
        return tpcMqMessageMapper.listReliableMessageVoWithPage(messageQueryDto);
    }

    @Override
    public List<TpcMessageVo> listReliableMessageVo(final List<Long> messageIdList) {
        return tpcMqMessageMapper.listReliableMessageVo(messageIdList);
    }

    private void resendMessage(TpcMqMessage message) {
        if (message == null) {
            throw new TpcBizException(ErrorCodeEnum.TPC10050002);
        }
        tpcMqMessageMapper.addTaskExeCountById(message.getId());
        //TODO 记录重发日志 1.系统自动重发 2.人工重发
        this.directSendMessage(message.getMessageBody(), message.getMessageTopic(), message.getMessageTag(), message.getMessageKey(), message.getProducerGroup(), message.getDelayLevel());
    }
}