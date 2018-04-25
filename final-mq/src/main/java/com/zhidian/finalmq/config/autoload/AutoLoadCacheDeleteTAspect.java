package com.zhidian.finalmq.config.autoload;

import com.jarvis.cache.annotation.CacheDeleteTransactional;
import com.jarvis.cache.aop.aspectj.AspectjAopInterceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 扫描@CacheDeleteTransactional注解
 */
@Aspect
@Component
public class AutoLoadCacheDeleteTAspect {


    @Autowired
    private AspectjAopInterceptor aspectjAopInterceptor;

    /**
     * 拦截带@CacheDeleteTransactional注解的方法
     */
    @Pointcut("execution(* com.zhidian.finalmq.service..*.*(..)) && @annotation(cacheDeleteTransactional)")
    public void aspect(CacheDeleteTransactional cacheDeleteTransactional) {
    }

    @Around(value = "aspect(cacheDeleteTransactional)", argNames = "proceedingJoinPoint,cacheDeleteTransactional")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, CacheDeleteTransactional cacheDeleteTransactional) throws Throwable {
        return aspectjAopInterceptor.deleteCacheTransactional(proceedingJoinPoint, cacheDeleteTransactional);
    }


}