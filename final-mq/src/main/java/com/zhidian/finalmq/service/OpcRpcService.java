package com.zhidian.finalmq.service;


import com.zhidian.finalmq.base.dto.MessageQueryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//import com.zhidian.finalmq.model.dto.robot.ChatRobotMsgDto;

/**
 * The class Opc rpc service.
 *
 * @author paascloud.net @gmail.com
 */
@Slf4j
@Component
public class OpcRpcService {

    //	@Resource
//	private DingtalkFeignApi dingtalkFeignApi;
//    @Resource
//    private OpcOssFeignApi opcOssFeignApi;
//    @Resource
//    private OpcMqMessageFeignApi opcMqMessageFeignApi;

    /**
     * Send chat robot msg boolean.
     *
     * @param chatRobotMsgDto the chat robot msg dto
     *
     * @return the boolean
     */
//	public boolean sendChatRobotMsg(ChatRobotMsgDto chatRobotMsgDto) {
//		Wrapper<Boolean> result = dingtalkFeignApi.sendChatRobotMsg(chatRobotMsgDto);
//		return result.getResult();
//	}

    /**
     * Delete expire file.
     */
    @Deprecated
    public void deleteExpireFile() {
//		opcOssFeignApi.deleteExpireFile();
    }

//    @Deprecated
//    public Wrapper<PageInfo<MqMessageVo>> queryMessageListWithPage(final MessageQueryDto messageQueryDto) {
//        Wrapper<PageInfo<MqMessageVo>> wrapper = opcMqMessageFeignApi.queryMessageListWithPage(messageQueryDto);
//        if (wrapper == null) {
//            log.error("查询消息记录 失败 result is null");
//            throw new TpcBizException(ErrorCodeEnum.GL99990002);
//        }
//        return wrapper;
//        return null;
//    }
}