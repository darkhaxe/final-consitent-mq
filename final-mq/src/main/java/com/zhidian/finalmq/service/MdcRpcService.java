package com.zhidian.finalmq.service;


import com.github.pagehelper.PageInfo;
import com.zhidian.finalmq.base.dto.MessageQueryDto;
import com.zhidian.finalmq.base.dto.MqMessageVo;
import com.zhidian.finalmq.base.wrapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//import com.paascloud.base.dto.MessageQueryDto;
//import com.paascloud.base.dto.MqMessageVo;
//import com.paascloud.base.enums.ErrorCodeEnum;
//import com.paascloud.wrapper.Wrapper;

/**
 * The class Mdc rpc service.
 *
 * @author paascloud.net @gmail.com
 */
@Slf4j
@Service
public class MdcRpcService {
//	@Resource
//	private MdcMqMessageFeignApi mdcMqMessageFeignApi;

	public Wrapper<PageInfo<MqMessageVo>> queryMessageListWithPage(final MessageQueryDto messageQueryDto) {
		throw new RuntimeException("unsupported method");

//		Wrapper<PageInfo<MqMessageVo>> wrapper = mdcMqMessageFeignApi.queryMessageListWithPage(messageQueryDto);
//		if (wrapper == null) {
//			log.error("查询消息记录. 失败 result is null");
//			throw new TpcBizException(ErrorCodeEnum.GL99990002);
//		}
//		return wrapper;
	}
}