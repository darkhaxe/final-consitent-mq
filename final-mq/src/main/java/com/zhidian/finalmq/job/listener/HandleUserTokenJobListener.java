package com.zhidian.finalmq.job.listener;

//import com.dangdang.ddframe.job.executor.ShardingContexts;
//import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
//import com.zhidian.finalmq.model.dto.robot.ChatRobotMsgDto;
//import com.zhidian.finalmq.model.factory.ChatRobotMsgFactory;
//import com.zhidian.finalmq.service.OpcRpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * The class Handle user token job listener.
 *
 * @author paascloud.net @gmail.com
 */
@Slf4j
public class HandleUserTokenJobListener{}
//public class HandleUserTokenJobListener implements ElasticJobListener {

//	@Resource
//	private OpcRpcService opcRpcService;
//
//	private AtomicLong startTime = new AtomicLong();
//	private AtomicLong endTimestamp = new AtomicLong();
//	@Value("paascloud.dingTalk.webhookToken.jobListener")
//	private String webhookToken;
//
//	/**
//	 * Before job executed.
//	 *
//	 * @param shardingContexts the sharding contexts
//	 */
//	@Override
//	public void beforeJobExecuted(ShardingContexts shardingContexts) {
//		log.info("beforeJobExecuted - shardingContexts={}", shardingContexts);
//		startTime.set(System.currentTimeMillis());
//	}
//
//	/**
//	 * After job executed.
//	 *
//	 * @param shardingContexts the sharding contexts
//	 */
//	@Override
//	public void afterJobExecuted(ShardingContexts shardingContexts) {
//		log.info("afterJobExecuted - shardingContexts={}", shardingContexts);
//		endTimestamp.set(System.currentTimeMillis());
//		long exeTime = endTimestamp.get() - startTime.get();
//		String message = String.format("jobName: %s | 执行完毕, 总耗时: %s", shardingContexts.getJobName(), exeTime + "毫秒");
//		this.sendDingTalk(message);
//	}
//
//	private void sendDingTalk(String message) {
//		log.info("开始发送消息. message={}", message);
//		ChatRobotMsgDto chatRobotMsgDto = ChatRobotMsgFactory.createChatRobotTextMsg(webhookToken, message, false, null);
//		boolean result = opcRpcService.sendChatRobotMsg(chatRobotMsgDto);
//		if (result) {
//			log.info("发送消息成功. message={}", message);
//		} else {
//			log.error("发送消息失败. message={}", message);
//		}
//	}
//}