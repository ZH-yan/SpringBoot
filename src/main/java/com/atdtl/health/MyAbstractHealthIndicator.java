package com.atdtl.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * <p>自定义健康端点</p>
 * <p>
 *     第二种方式；继承AbstractHeathIndicator抽象类
 * </p>
 *
 * @author Administrator
 * @since 2018/7/25 11:28
 */
@Component("my2")
public class MyAbstractHealthIndicator extends AbstractHealthIndicator {

    private static final String VERSION = "v1.0.0";

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        int code = check();
        if (code != 0) {
            builder.down().withDetail("code", code).withDetail("version", VERSION).build();
        }
        builder.withDetail("code", code).withDetail("version", VERSION).up().build();
    }

    private int check() {
        return 0;
    }
}
