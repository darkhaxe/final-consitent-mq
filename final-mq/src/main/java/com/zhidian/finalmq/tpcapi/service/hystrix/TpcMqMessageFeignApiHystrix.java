package com.zhidian.finalmq.tpcapi.service.hystrix;

import com.zhidian.finalmq.base.wrapper.Wrapper;
import com.zhidian.finalmq.model.dto.TpcMqMessageDto;
import com.zhidian.finalmq.tpcapi.service.TpcMqMessageFeignApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * The class Tpc mq message feign api hystrix.
 *
 * @author paascloud.net @gmail.com
 */
@Component
@Slf4j
public class TpcMqMessageFeignApiHystrix implements TpcMqMessageFeignApi {

    @Override
    public Wrapper saveMessageWaitingConfirm(final TpcMqMessageDto mqMessageDto) {
        log.error("saveMessageWaitingConfirm - 服务降级. mqMessageDto={}", mqMessageDto);
        return null;
    }

    @Override
    public Wrapper confirmAndSendMessage(final String messageKey) {
        return null;
    }

    @Override
    public Wrapper saveAndSendMessage(final TpcMqMessageDto mqMessageDto) {
        return null;
    }

    @Override
    public Wrapper directSendMessage(final TpcMqMessageDto mqMessageDto) {
        return null;
    }

    @Override
    public Wrapper deleteMessageByMessageKey(final String messageKey) {
        return null;
    }

    @Override
    public Wrapper confirmReceiveMessage(final String cid, final String messageKey) {
        return null;
    }

    @Override
    public Wrapper confirmConsumedMessage(final String cid, final String messageKey) {
        return null;
    }
}
