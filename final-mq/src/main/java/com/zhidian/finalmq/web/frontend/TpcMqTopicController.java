package com.zhidian.finalmq.web.frontend;

import com.zhidian.finalmq.base.BaseController;
import com.zhidian.finalmq.base.annotation.LogAnnotation;
import com.zhidian.finalmq.base.dto.LoginAuthDto;
import com.zhidian.finalmq.base.dto.UpdateStatusDto;
import com.zhidian.finalmq.base.wrapper.WrapMapper;
import com.zhidian.finalmq.base.wrapper.Wrapper;
import com.zhidian.finalmq.model.domain.TpcMqTopic;
import com.zhidian.finalmq.model.vo.TpcMqTopicVo;
import com.zhidian.finalmq.service.TpcMqTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * The class Tpc mq topic controller.
 *
 * @author paascloud.net @gmail.com
 */
@RestController
@RequestMapping(value = "/topic", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "WEB - TpcMqTopicController", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TpcMqTopicController extends BaseController {

	@Resource
	private TpcMqTopicService tpcMqTopicService;


	/**
	 * 查询MQ topic列表.
	 *
	 * @param tpcMqTopic the tpc mq topic
	 *
	 * @return the wrapper
	 */
	@PostMapping(value = "/queryTopicListWithPage")
	@ApiOperation(httpMethod = "POST", value = "查询MQ topic列表")
	public Wrapper<List<TpcMqTopicVo>> queryTopicListWithPage(@ApiParam(name = "topic", value = "MQ-Topic") @RequestBody TpcMqTopic tpcMqTopic) {

		logger.info("查询角色列表tpcMqTopicQuery={}", tpcMqTopic);
		List<TpcMqTopicVo> list = tpcMqTopicService.listWithPage(tpcMqTopic);
		return WrapMapper.ok(list);
	}

	/**
	 * 修改topic状态.
	 *
	 * @param updateStatusDto the update status dto
	 *
	 * @return the wrapper
	 */
	@PostMapping(value = "/modifyStatusById")
	@ApiOperation(httpMethod = "POST", value = "修改topic状态")
	@LogAnnotation
	public Wrapper modifyTopicStatusById(@ApiParam(value = "修改topic状态") @RequestBody UpdateStatusDto updateStatusDto) {
		logger.info("修改topic状态 updateStatusDto={}", updateStatusDto);
		Long roleId = updateStatusDto.getId();

		LoginAuthDto loginAuthDto = getLoginAuthDto();

		TpcMqTopic topic = new TpcMqTopic();
		topic.setId(roleId);
		topic.setStatus(updateStatusDto.getStatus());
		topic.setUpdateInfo(loginAuthDto);

		int result = tpcMqTopicService.update(topic);
		return super.handleResult(result);
	}
}
