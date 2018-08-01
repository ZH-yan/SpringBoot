package com.atdtl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 锁的注解
 *
 * @author Administrator
 * @since 2018/7/27 16:02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LocalLock {

    String key() default "";

    /**
     *  过期时间 TODO 由于用的 guava 暂时忽略此属性；集成redis需用到
     *
     * @return
     */
    int expire() default 5;
}
