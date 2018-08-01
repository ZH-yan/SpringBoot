package com.atdtl.interceptor;

import com.atdtl.annotation.CacheLock;
import com.atdtl.annotation.CacheParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * key生成策略（实现）
 *      主要是解析带 CacheLock 注解的属性，获取对应的属性值，生成一个全新的缓存key
 *
 * @author Administrator
 * @since 2018/7/28 12:33
 */
public class LockKeyGenerator implements CacheKeyGenerator {

    /**
     *  解析带 CacheLock 注解的属性，获取对应的属性值，生成一个全新的缓存key
     *
     * @param pjp
     * @return
     */
    @Override
    public String getLockKey(ProceedingJoinPoint pjp) {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lockAnnotation = method.getAnnotation(CacheLock.class);

        final Object[] args = pjp.getArgs();
        final Parameter[] parameters = method.getParameters();

        StringBuilder builder = new StringBuilder();
        // TODO: 2018/7/28 默认解析带 CacheParam 注解的属性，如果没有尝试着实体对象中的
        for (int i = 0; i < parameters.length; i++) {
            final CacheParam annotation = parameters[i].getAnnotation(CacheParam.class);
            if (annotation == null){
                continue;
            }
            builder.append(lockAnnotation.delimiter()).append(args[i]);
        }

        if (StringUtils.isEmpty(builder.toString())){
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                final Object object = args[i];
                final Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    final CacheParam annotation = field.getAnnotation(CacheParam.class);
                    if (annotation == null){
                        continue;
                    }
                    field.setAccessible(true);
                    builder.append(lockAnnotation.delimiter()).append(args[i]);
                }
            }
        }

        return lockAnnotation.prefix() + builder.toString();
    }
}
