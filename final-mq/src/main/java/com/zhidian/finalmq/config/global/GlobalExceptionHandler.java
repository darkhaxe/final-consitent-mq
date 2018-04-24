package com.zhidian.finalmq.config.global;

import com.alibaba.fastjson.JSON;
import com.zhidian.cloud.common.core.service.SendEmailService;
import com.zhidian.cloud.common.exception.BusinessException;
import com.zhidian.cloud.common.utils.common.JsonResult;
//import com.zhidian.order.api.module.exceptions.StockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 创建者:周广
 * 创建日期:2016/6/18 14:44
 * 文件简述:统一异常处理类
 * 更新时间:
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SendEmailService sendEmailService;

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JsonResult defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error("系统异常或错误", e);
        if (e instanceof BusinessException) {
            return new JsonResult(((BusinessException) e).getCode(), e.getMessage());
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException argumentNotValidException = ((MethodArgumentNotValidException) e);
            StringBuilder sb = new StringBuilder();
            try {
                sb.append(JSON.toJSONString(argumentNotValidException.getBindingResult().getTarget()));
            } catch (Exception ignore) {
            }
            FieldError fieldError = argumentNotValidException.getBindingResult().getFieldError();
            sb.append("<br/>").append(fieldError.getDefaultMessage());
            sendEmailService.sendEmail("调用接口参数错误", sb.toString());
            return new JsonResult(JsonResult.ERR, fieldError.getDefaultMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException exception = (HttpRequestMethodNotSupportedException) e;
            return new JsonResult<>(JsonResult.ERR, "不支持:" + exception.getMethod() + "请求，此接口支持:" + exception.getSupportedHttpMethods());
        }
        return JsonResult.FAIL;
    }


}
