package com.zhidian.finalmq.sdk.aspect;

import com.zhidian.finalmq.enums.ErrorCodeEnum;
import com.zhidian.finalmq.exceptions.TpcBizException;
import com.zhidian.finalmq.sdk.annotation.MqConsumerStore;
import com.zhidian.finalmq.sdk.model.domain.MqMessageData;
import com.zhidian.finalmq.sdk.model.enums.MqMessageTypeEnum;
import com.zhidian.finalmq.sdk.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;


/**
 * The class Mq consumer store aspect.
 *
 * @author paascloud.net @gmail.com
 */
@Slf4j
@Aspect
public class MqConsumerStoreAspect {

    private static final String CONSUME_SUCCESS = "CONSUME_SUCCESS";
    @Resource
    private MqMessageService mqMessageService;
    //application.yml定义
    @Value("${paascloud.aliyun.rocketMq.consumerGroup}")
    private String consumerGroup;

    /**
     * Add exe time annotation pointcut.
     */
    @Pointcut("@annotation(com.zhidian.finalmq..annotation.MqConsumerStore)")
    public void mqConsumerStoreAnnotationPointcut() {

    }

    /**
     * Add exe time method object.
     *
     * @param joinPoint the join point
     * @return the object
     * @throws Throwable the throwable
     */
    @Around(value = "mqConsumerStoreAnnotationPointcut()")
    public Object processMqConsumerStoreJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("processMqConsumerStoreJoinPoint - 线程id={}", Thread.currentThread().getId());
        Object result;
        long startTime = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();
        //获取切面方法的注解
        MqConsumerStore annotation = getAnnotation(joinPoint);
        //切面方法设置的值
        boolean isStorePreStatus = annotation.storePreStatus();
        List<MessageExt> messageExtList;
        if (args == null || args.length == 0) {
            throw new TpcBizException(ErrorCodeEnum.TPC10050005);
        }

        if (!(args[0] instanceof List)) {
            throw new TpcBizException(ErrorCodeEnum.GL99990001);
        }

        try {
            messageExtList = (List<MessageExt>) args[0];
        } catch (Exception e) {
            log.error("processMqConsumerStoreJoinPoint={}", e.getMessage(), e);
            throw new TpcBizException(ErrorCodeEnum.GL99990001);
        }
//1.获取调用方的消息内容
        MqMessageData dto = this.getTpcMqMessageDto(messageExtList.get(0));
        final String messageKey = dto.getMessageKey();
        //2.
        if (isStorePreStatus) {
            //实际调用任务服务(tpc)的接口入库 todo 写入内容:
            //确认收到mq消息
            mqMessageService.confirmReceiveMessage(consumerGroup, dto);
        }
        String methodName = joinPoint.getSignature().getName();
        try {
            result = joinPoint.proceed();
            log.info("result={}", result);
            //调用方成功,在写入完成的记录;
            // 不成功,则不会有写入的记录; todo 写入内容:
            //mq消费成功
            if (CONSUME_SUCCESS.equals(result.toString())) {
                mqMessageService.saveAndConfirmFinishMessage(consumerGroup, messageKey);
            }
        } catch (Exception e) {
            log.error("发送可靠消息, 目标方法[{}], 出现异常={}", methodName, e.getMessage(), e);
            throw e;
        } finally {
            log.info("发送可靠消息 目标方法[{}], 总耗时={}", methodName, System.currentTimeMillis() - startTime);
        }
        return result;
    }

    private MqConsumerStore getAnnotation(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(MqConsumerStore.class);
    }

    private MqMessageData getTpcMqMessageDto(MessageExt messageExt) {
        MqMessageData data = new MqMessageData();
        data.setMessageBody(new String(messageExt.getBody()));
        data.setMessageKey(messageExt.getKeys());
        data.setMessageTag(messageExt.getTags());
        data.setMessageTopic(messageExt.getTopic());
        data.setMessageType(MqMessageTypeEnum.CONSUMER_MESSAGE.messageType());
        return data;
    }
}
