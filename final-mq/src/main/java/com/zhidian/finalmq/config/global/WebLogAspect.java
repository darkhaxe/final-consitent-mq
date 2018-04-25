package com.zhidian.finalmq.config.global;

import com.alibaba.fastjson.JSON;
import com.zhidian.cloud.common.utils.common.UUIDUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：周广
 * 创建时间：2016/12/29 0029
 * 必要描述:
 */
@Aspect
@Component
public class WebLogAspect {
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    ThreadLocal<String> tag = new ThreadLocal<String>();//本次请求标记
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${appEnv}")
    private int appEnv;

    /**
     * 定义一个切入点.
     * 解释下：
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 任意包名
     * ~ 第三个 * 代表任意方法.
     * ~ 第四个 * 定义在web包或者子包
     * ~ 第五个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution(public * com.zhidian..web..*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        if (appEnv < 4) {
            startTime.set(System.currentTimeMillis());
            String uuid = UUIDUtil.generateUUID();
            tag.set(uuid);

            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();


            // 记录下请求内容
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n本次请求标识：").append(uuid).append("\n");
            stringBuilder.append("URL : ").append(request.getRequestURL().toString()).append("\n");
            stringBuilder.append("HTTP_METHOD : ").append(request.getMethod()).append("\n");
            String sessionId = request.getHeader("sessionId");
            if (sessionId != null) {
                stringBuilder.append("SESSION_ID : ").append(sessionId).append("\n");
            }
            stringBuilder.append("IP : ").append(request.getRemoteAddr()).append("\n");
            stringBuilder.append("CLASS_METHOD : ").append(joinPoint.getSignature().getDeclaringTypeName()).append(".").append(joinPoint.getSignature().getName()).append("\n");

            stringBuilder.append("ARGS : ");
            Object[] params = joinPoint.getArgs();

            try {
                for (Object param : params) {
                    if (param instanceof HttpServletRequest) {
                    } else {
                        stringBuilder.append(JSON.toJSONString(param)).append("\n");
                    }
                }
            } catch (Exception ignored) {
            }

            logger.info(stringBuilder.toString());

        }
    }

    @AfterReturning("webLog()")
    public void doAfterReturning(JoinPoint joinPoint) {
        if (appEnv < 4) {
            // 处理完请求，返回内容
            logger.info("\n请求标记：{} 处理完成 耗时（毫秒）: {}", tag.get(), (System.currentTimeMillis() - startTime.get()));
        }

    }

}