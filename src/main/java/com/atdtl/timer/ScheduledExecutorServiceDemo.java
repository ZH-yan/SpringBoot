package com.atdtl.timer;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Timer运行多个 TimeTask 时，只要有其中一个因任务报错而没有捕捉到异常，其他任务变回自定只能被终止进行
 *  使用 scheduledExecutorService 可以规避这个问题
 *
 * @author Administrator
 * @since 2018/7/26 11:03
 */
public class ScheduledExecutorServiceDemo {

    public static void main(String[] args) {
        ScheduledExecutorService Service = Executors.newScheduledThreadPool(10);

        /**
         * 参数：1.具体执行的任务 2.首次执行的延长时间
         *      3.任务执行间隔    4.间隔时间单位
         */
        Service.scheduleAtFixedRate(() -> System.out.println("执行任务A：" + LocalDateTime.now()), 0, 3, TimeUnit.SECONDS);
    }
}