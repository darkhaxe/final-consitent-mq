package com.zhidian.finalmq.config.global;

import com.alibaba.fastjson.JSON;
import com.zhidian.cloud.common.exception.BusinessException;
import com.zhidian.cloud.common.utils.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

//import com.zhidian.order.api.module.exceptions.StockException;

/**
 * 创建者:周广
 * 创建日期:2016/6/18 14:44
 * 文件简述:统一异常处理类
 * 更新时间:
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
//    @Autowired
//    private SendEmailService sendEmailService;

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
            log.error("调用接口参数错误 {}", sb.toString());
//            sendEmailService.sendEmail("调用接口参数错误", sb.toString());
            return new JsonResult(JsonResult.ERR, fieldError.getDefaultMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException exception = (HttpRequestMethodNotSupportedException) e;
            return new JsonResult<>(JsonResult.ERR, "不支持:" + exception.getMethod() + "请求，此接口支持:" + exception.getSupportedHttpMethods());
        }
        return JsonResult.FAIL;
    }


}
