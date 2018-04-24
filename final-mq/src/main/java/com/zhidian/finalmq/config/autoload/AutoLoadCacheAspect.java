package com.zhidian.finalmq.config.autoload;

import com.jarvis.cache.annotation.Cache;
import com.jarvis.cache.aop.aspectj.AspectjAopInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 扫描@Cache注解
 */
@Aspect
@Component
public class AutoLoadCacheAspect {


    @Autowired
    private AspectjAopInterceptor aspectjAopInterceptor;

    /**
     * 如果@Cache不是用在接口中，可以按下面方法来配置
     */
    @Pointcut("execution(public !void com.zhidian.order.service..*.*(..)) && @annotation(cache)")
    public void aspect(Cache cache) {
    }

    @Around(value = "aspect(cache)", argNames = "proceedingJoinPoint,cache")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Cache cache) throws Throwable {
        return aspectjAopInterceptor.proceed(proceedingJoinPoint, cache);
    }


    @Pointcut("execution(* com.zhidian.order.dao.mapper*..*.*(..))")
    public void mapper() {
    }

    @Around("mapper()")//缓存新增aop
    public Object mapperAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return aspectjAopInterceptor.checkAndProceed(proceedingJoinPoint);
    }


}