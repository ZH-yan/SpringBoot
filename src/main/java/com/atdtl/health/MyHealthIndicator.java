package com.atdtl.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * <p>自定义健康端点</p>
 *  第一种方式：实现 HealthIndicator 接口，根据自己的需要判断返回的状态是 UP 还是 DOWN ，功能简单
 *
 * @author Administrator
 * @since 2018/7/25 11:00
 */
@Component("my1")
public class MyHealthIndicator implements HealthIndicator {

    private static final String VERSION = "v1.0.0";

    @Override
    public Health health() {
        int code = check();
        if (code != 0) {
            Health.down().withDetail("code",code).withDetail("version", VERSION).build();
        }

        return Health.up().withDetail("code",code).withDetail("version", VERSION).up().build();
    }

    private int check() {
        return 0;
    }
}
