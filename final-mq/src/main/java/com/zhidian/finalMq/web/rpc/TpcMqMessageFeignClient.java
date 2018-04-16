package com.zhidian.finalMq.web.rpc;

import com.paascloud.core.support.BaseController;
import com.zhidian.finalMq.model.dto.TpcMqMessageDto;
import com.zhidian.finalMq.service.TpcMqMessageFeignApi;
import com.zhidian.finalMq.service.TpcMqMessageService;
//import com.paascloud.wrapper.WrapMapper;
//import com.paascloud.wrapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * The class Tpc mq message feign client.
 *
 * @author paascloud.net @gmail.com
 */
@RestController
@Api(value = "API - TpcMqMessageFeignClient", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TpcMqMessageFeignClient extends BaseController implements TpcMqMessageFeignApi {
	@Resource
	private TpcMqMessageService tpcMqMessageService;

	@Override
	@ApiOperation(httpMethod = "POST", value = "预存储消息")
	public Wrapper saveMessageWaitingConfirm(@RequestBody TpcMqMessageDto mqMessageDto) {
		logger.info("预存储消息. mqMessageDto={}", mqMessageDto);
		tpcMqMessageService.saveMessageWaitingConfirm(mqMessageDto);
		return WrapMapper.ok();
	}

	@Override
	@ApiOperation(httpMethod = "POST", value = "确认并发送消息")
	public Wrapper confirmAndSendMessage(@RequestParam("messageKey") String messageKey) {
		logger.info("确认并发送消息. messageKey={}", messageKey);
		tpcMqMessageService.confirmAndSendMessage(messageKey);
		return WrapMapper.ok();
	}

	@Override
	@ApiOperation(httpMethod = "POST", value = "存储并发送消息")
	public Wrapper saveAndSendMessage(@RequestBody TpcMqMessageDto message) {
		logger.info("存储并发送消息. mqMessageDto={}", message);
		tpcMqMessageService.saveAndSendMessage(message);
		return WrapMapper.ok();
	}

	@Override
	@ApiOperation(httpMethod = "POST", value = "直接发送消息")
	public Wrapper directSendMessage(@RequestBody TpcMqMessageDto messageDto) {
		logger.info("直接发送消息. mqMessageDto={}", messageDto);
		tpcMqMessageService.directSendMessage(messageDto.getMessageBody(), messageDto.getMessageTopic(), messageDto.getMessageTag(), messageDto.getMessageKey(), messageDto.getProducerGroup(), messageDto.getDelayLevel());
		return WrapMapper.ok();
	}

	@Override
	@ApiOperation(httpMethod = "POST", value = "根据消息ID删除消息")
	public Wrapper deleteMessageByMessageKey(@RequestParam("messageKey") String messageKey) {
		logger.info("根据消息ID删除消息. messageKey={}", messageKey);
		tpcMqMessageService.deleteMessageByMessageKey(messageKey);
		return WrapMapper.ok();
	}

	@Override
	@ApiOperation(httpMethod = "POST", value = "确认收到消息")
	public Wrapper confirmReceiveMessage(@RequestParam("cid") final String cid, @RequestParam("messageKey") final String messageKey) {
		logger.info("确认收到消息. cid={}, messageKey={}", cid, messageKey);
		tpcMqMessageService.confirmReceiveMessage(cid, messageKey);
		return WrapMapper.ok();
	}

	@Override
	@ApiOperation(httpMethod = "POST", value = "确认消费消息")
	public Wrapper confirmConsumedMessage(@RequestParam("cid") final String cid, @RequestParam("messageKey") final String messageKey) {
		logger.info("确认完成消费消息. cid={}, messageKey={}", cid, messageKey);
		tpcMqMessageService.confirmConsumedMessage(cid, messageKey);
		return WrapMapper.ok();
	}
}
