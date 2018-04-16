package com.zhidian.finalMq..config.aop;

import com.alibaba.fastjson.JSONObject;
import com.zhidian.cloud.common.config.mail.EmailConfiguration;
import com.zhidian.cloud.common.exception.BusinessException;
import com.zhidian.cloud.common.model.bo.MailBodyVo;
import com.zhidian.cloud.common.utils.aop.AbstractAspectj;
import com.zhidian.cloud.common.utils.http.IpUtil;
import com.zhidian.cloud.common.utils.time.DateTimeUtil;
import com.zhidian.order.api.module.exceptions.StockException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Enumeration;


/**
 * 作者：周广
 * 创建时间：2017/05/17 0008
 * 必要描述:
 */
@Aspect
@Component
public class ServiceExceptionAspectj extends AbstractAspectj {

    @Autowired
    private EmailConfiguration emailConfiguration;
    //收件地址list
    @Value("${failure_process_email_address}")
    private String[] receiveAddressList;
    @Value("${defined.projectDesc}")
    private String mailTitle;
    @Value("${appEnv}")
    private int appEnv;

    /**
     * 在方法出现异常时会执行的代码
     * 可以访问到异常对象，可以指定在出现特定异常时在执行通知代码
     */
    @AfterThrowing(value = "execution(* com.zhidian.order.service..*(..))", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        if (verifyExceptionType(ex)) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            StringBuilder content = new StringBuilder();
            content.append("发生时间：").append(DateTimeUtil.getSimpleDateTime()).append(br);
            content.append("服务器IP：").append(IpUtil.getLocalIP()).append(br);
            content.append("类名：").append(joinPoint.getSignature().getDeclaringTypeName()).append(br);
            content.append("方法名称：").append(joinPoint.getSignature().getName()).append(br);
            content.append("请求头部：").append(getRequestHeader(request)).append(br);
            content.append("请求参数：").append(conventTOStr(joinPoint.getArgs())).append(br).append(br);
            content.append(br);
            content.append("异常堆栈：").append(getStackTrace(ex));
            MailBodyVo body = new MailBodyVo();
            body.addAllTo(Arrays.asList(receiveAddressList));
            body.setTitle(convertEnvDesc(appEnv) + mailTitle + (ex.getMessage() == null ? "" : ex.getMessage()));
            body.setContent(content.toString());
            emailConfiguration.newEmailService().sendTextMail(body);
        }
    }

    //获取请求头部
    private String getRequestHeader(HttpServletRequest request) {
        Enumeration<String> headNames = request.getHeaderNames();
        JSONObject head = new JSONObject();
        while (headNames.hasMoreElements()) {
            String name = headNames.nextElement();
            head.put(name, request.getHeader(name));
        }
        return head.toJSONString();
    }

    //验证异常类型，需要通知的返回true 否则false
    protected boolean verifyExceptionType(Exception ex) {
        if (ex instanceof BusinessException) {
            return false;
        } else if (ex instanceof StockException) {
            return false;
        } else if (ex instanceof UndeclaredThrowableException) {
            try {
                if (ex.getCause().getCause() instanceof BusinessException) {
                    return false;
                }
            } catch (Exception ignore) {
                return true;
            }
        } else if (ex.getCause() != null && ex.getCause() instanceof BusinessException) {
            return false;
        }
        return true;
    }

}
