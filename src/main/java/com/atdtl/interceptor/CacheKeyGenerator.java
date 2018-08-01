package com.atdtl.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * key 生成器（key 生成策略接口）
 *
 * @author Administrator
 * @since 2018/7/28 12:29
 */
public interface CacheKeyGenerator {

    /**
     * 获取AOP参数，生成指定缓存key
     *
     * @param pjp
     * @return 缓存key
     */
    String getLockKey(ProceedingJoinPoint pjp);
}
