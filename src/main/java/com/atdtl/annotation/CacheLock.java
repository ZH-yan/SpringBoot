package com.atdtl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 *  CacheLock 注解
 *
 * @author Administrator
 * @since 2018/7/28 12:09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    /**
     * redis 锁 key 的前缀
     *
     * @return  redis 锁 key 的前缀
     */
    String prefix() default "";

    /**
     * 过期秒数，默认为5秒
     *
     * @return  轮询锁的时间
     */
    int expire() default 20;

    /**
     * 超时时间单位
     *
     * @return  秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * <p>key 的分隔符（默认为：）</p>
     * <p>生成的key: N:SO1008:500</p>
     *
     * @return  String
     */
    String delimiter() default ":";

}
