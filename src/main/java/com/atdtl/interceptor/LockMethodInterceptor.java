package com.atdtl.interceptor;

import com.atdtl.annotation.CacheLock;
import com.atdtl.annotation.LocalLock;
import com.atdtl.utils.RedisLockHelper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.util.StringUtils;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于 本地缓存
 * redis 方案
 *
 * @author Administrator
 * @since 2018/7/27 16:07
 */
@Aspect
@Configuration
public class LockMethodInterceptor {

    final private RedisLockHelper redisLockHelper;
    final private  CacheKeyGenerator cacheKeyGenerator;

    @Autowired
    public LockMethodInterceptor(RedisLockHelper redisLockHelper, CacheKeyGenerator cacheKeyGenerator) {
        this.redisLockHelper = redisLockHelper;
        this.cacheKeyGenerator =cacheKeyGenerator;
    }

    /**
     * 基于本地缓存
     */
    public static final Cache<String, Object> CACHES = CacheBuilder.newBuilder()
            // 最大缓存 100 个
            .maximumSize(1000)
            // 设置写缓存后 5 秒钟过期
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    /**
     *  1.基于本地缓存： LocalLock
     *
     * @param pjp
     * @return
     */
    @Around("execution(public * *(..)) && @annotation(com.atdtl.annotation.LocalLock)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LocalLock localLock = method.getAnnotation(LocalLock.class);
        String key = getKey(localLock.key(), pjp.getArgs());
        if (!StringUtils.isEmpty(key)) {
            if (CACHES.getIfPresent(key) != null) {
                throw new RuntimeException("请勿重复请求");
            }
            // 如果是第一次请求，就将 key 当前对象压入缓存中
            CACHES.put(key, key);
        }

        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException("服务器异常");
        } finally {

        }

    }

    /**
     *  分布式锁：缓存到redis
     * @param pjp
     * @return
     */
    @Around("execution(public * *(..)) && @annotation(com.atdtl.annotation.CacheLock)")
    public Object interceptor1(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lock = method.getAnnotation(CacheLock.class);
        if (StringUtils.isEmpty(lock.prefix())) {
            throw new RuntimeException("lock key don't null...");
        }
        final String lockKey = cacheKeyGenerator.getLockKey(pjp);
        String value = UUID.randomUUID().toString();

        try {
            final boolean success = redisLockHelper.lock(lockKey, value, lock.expire(), lock.timeUnit());
            if (!success) {
                throw new RuntimeException("重复提交");
            }

            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException("系统异常");
            }
        } finally {
            // TODO: 2018/7/30  如果演示的话需要注释该代码；实际应该放开
            // redisLockHelper.unlock(lockKey, value);
        }
    }

    /**
     *  key 的生成策略，如果想灵活可以写成接口与实现类的方式
     *
     * @param keyExpress    表达式
     * @param args          参数
     * @return              生成的key
     */
    private String getKey(String keyExpress, Object[] args) {
        for (int i = 0; i < args.length; i++) {
            keyExpress = keyExpress.replace("arg[" + i + "]", args[i].toString());
        }
        return keyExpress;
    }
}
