package com.atdtl.utils;

import com.atdtl.config.RedisCacheAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.StringUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * redis 方案 ：通过封装成API方式调用 提高灵活度
 *      由于 redis 是线程安全的，利用它很容易实现分布式锁，如:opsForValue().setIfAbsent(key,value),
 *      它的作用就是：如果缓存中没有当前key则进行缓存的同时返回true反之亦然，当缓存过后给key再设置过期时间，防止
 *      因为系统崩溃而导致锁迟迟不释放形成死锁。
 *      当返回true，则代表获取到锁了；在锁未释放时进行异常抛出
 *
 * @author Administrator
 * @since 2018/7/28 13:09
 */
@Configuration
@AutoConfigureAfter(RedisCacheAutoConfiguration.class)
public class RedisLockHelper {

    private static final String DELIMITER = "|";

    /**
     * 通过注入的方式分配
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(10);

    private final StringRedisTemplate redisTemplate;

    public RedisLockHelper(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     *  获取锁（存在死锁的风险）
     *
     * @param lockKey   lockKey
     * @param value     value
     * @param time      超时时间
     * @param unit      过期时间
     * @return          true or false
     */
    public boolean tryLock(final String lockKey, final String value, final long time, final TimeUnit unit) {
        return redisTemplate.execute((RedisCallback<Boolean>)  connection -> connection.set(lockKey.getBytes(), value.getBytes(),
                Expiration.from(time, unit), RedisStringCommands.SetOption.SET_IF_ABSENT));
    }

    /**
     *  获取锁
     * @param lockKey       lockKey
     * @param uuid          UUID
     * @param timeout       超时时间
     * @param unit          过期时间
     * @return              true or false
     */
    public boolean lock(String lockKey, final String uuid, long timeout, final TimeUnit unit) {
        final long milliseconds = Expiration.from(timeout, unit).getExpirationTimeInMilliseconds();
        boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, (System.currentTimeMillis() + milliseconds) + DELIMITER + uuid);
        if (success) {  // 不存在
            redisTemplate.expire(lockKey, timeout, TimeUnit.SECONDS);
        } else {    // 存在
            String ordVal = redisTemplate.opsForValue().getAndSet(lockKey, (System.currentTimeMillis() + milliseconds) + DELIMITER + uuid);
            final String[] ordValues = ordVal.split(Pattern.quote(DELIMITER));
            if (Long.parseLong(ordValues[0]) + 1 < System.currentTimeMillis()){
                return true;
            }
        }
        return success;
    }

    public void unlock(String lockKey, String value) {
        unlock(lockKey, value, 0, TimeUnit.MILLISECONDS);
    }

    /**
     *  延迟unlock
     * @param lockKey   key
     * @param uuid
     * @param delaytime 延迟时间
     * @param unit      时间单位
     */
    private void unlock(final String lockKey, final String uuid, long delaytime, TimeUnit unit) {
        if (StringUtils.isEmpty(lockKey)){
            return;
        }
        if (delaytime > 0) {
            doUnlock(lockKey, uuid);
        } else {
            EXECUTOR_SERVICE.schedule(() -> doUnlock(lockKey, uuid), delaytime, unit);
        }
    }

    /**
     *
     * @param lockKey   key
     * @param uuid      client(最好是唯一键的)
     */
    private void doUnlock(final String lockKey, final String uuid) {
        String val = redisTemplate.opsForValue().get(lockKey);
        final String[] values = val.split(Pattern.quote(DELIMITER));
        if (values.length <= 0) {
            return;
        }
        if (uuid.equals(values[1])) {
            redisTemplate.delete(lockKey);
        }
    }
}
