package com.zhidian.finalmq.config.autoload;

import com.jarvis.cache.annotation.CacheDelete;
import com.jarvis.cache.aop.aspectj.AspectjAopInterceptor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 扫描@CacheDelete注解
 */
@Aspect
@Component
@Order(1000)
public class AutoLoadCacheDeleteAspect {


    @Autowired
    private AspectjAopInterceptor aspectjAopInterceptor;

    /**
     * 如果@Cache不是用在接口中，可以按下面方法来配置
     */
    @Pointcut("execution(* com.zhidian.order.service..*.*(..)) && @annotation(cacheDelete)")
    public void cacheDeleteAspect(CacheDelete cacheDelete) {
    }

    @AfterReturning(value = "cacheDeleteAspect(cacheDelete)", returning = "retVal", argNames = "aopProxyChain,cacheDelete,retVal")
    public void cacheDelete(JoinPoint aopProxyChain, CacheDelete cacheDelete, Object retVal) throws Throwable {
        aspectjAopInterceptor.deleteCache(aopProxyChain, cacheDelete, retVal);
    }


    @Pointcut("execution(* com.zhidian.order.dao.mapper*..*.*(..))")
    public void mapper() {
    }

    @AfterReturning(pointcut = "mapper()", returning = "retVal")
    public void mapperDeleteCache(JoinPoint jp, Object retVal) throws Throwable {
        aspectjAopInterceptor.checkAndDeleteCache(jp, retVal);
    }

}
