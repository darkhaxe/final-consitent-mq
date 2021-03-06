package com.zhidian.finalmq.sdk.aspect;

import com.zhidian.finalmq.enums.ErrorCodeEnum;
import com.zhidian.finalmq.exceptions.TpcBizException;
import com.zhidian.finalmq.sdk.annotation.MqProducerStore;
import com.zhidian.finalmq.sdk.model.domain.MqMessageData;
import com.zhidian.finalmq.sdk.model.enums.DelayLevelEnum;
import com.zhidian.finalmq.sdk.model.enums.MqSendTypeEnum;
import com.zhidian.finalmq.sdk.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * The class Mq producer store aspect.
 *
 * @author paascloud.net @gmail.com
 */
@Slf4j
@Aspect
public class MqProducerStoreAspect {
    @Resource
    private MqMessageService mqMessageService;
    @Value("${paascloud.aliyun.rocketMq.producerGroup}")
    private String producerGroup;

    private static MqProducerStore getAnnotation(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(MqProducerStore.class);
    }

    /**
     * Add exe time annotation pointcut.
     */
    @Pointcut("@annotation(com.zhidian.finalmq..annotation.MqProducerStore)")
    public void mqProducerStoreAnnotationPointcut() {

    }

    /**
     * Add exe time method object.
     *
     * @param joinPoint the join point
     * @return the object
     */
    @Around(value = "mqProducerStoreAnnotationPointcut()")
    public Object processMqProducerStoreJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("processMqProducerStoreJoinPoint - 线程id={}", Thread.currentThread().getId());
        Object[] args = joinPoint.getArgs();
        MqProducerStore annotation = getAnnotation(joinPoint);
        MqSendTypeEnum type = annotation.sendType();
        int orderType = annotation.orderType().orderType();
        DelayLevelEnum delayLevelEnum = annotation.delayLevel();
        MqMessageData domain = Arrays.stream(args)
                //切面方法的参数,取出要发送的消息
                .filter(arg -> arg instanceof MqMessageData)
                .map(MqMessageData.class::cast)
                .findFirst()
                .orElseThrow(() -> new TpcBizException(ErrorCodeEnum.TPC10050005));

        domain.setOrderType(orderType);
        domain.setProducerGroup(producerGroup);
        //------------------方法执行前------------------
        // 入库一条记录,状态为等待确认
        if (type == MqSendTypeEnum.WAIT_CONFIRM) {
            if (delayLevelEnum != DelayLevelEnum.ZERO) {
                domain.setDelayLevel(delayLevelEnum.delayLevel());
            }
            mqMessageService.saveWaitConfirmMessage(domain);
        }
        //方法执行
        Object result = joinPoint.proceed();

        //------------------方法执行后的操作------------------
        if (type == MqSendTypeEnum.SAVE_AND_SEND) {
            // 入库一条记录,状态为等待确认
            mqMessageService.saveAndSendMessage(domain);//调用本地的mapper保存消息并发送
        } else if (type == MqSendTypeEnum.DIRECT_SEND) {
            mqMessageService.directSendMessage(domain);
        } else {
            mqMessageService.confirmAndSendMessage(domain.getMessageKey());
        }
        return result;
    }
}
