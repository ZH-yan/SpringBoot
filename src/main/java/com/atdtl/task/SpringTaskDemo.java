package com.atdtl.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 *  基于Spring 自带的 task
 *
 * @author Administrator
 * @since 2018/7/26 11:48
 */
// @Component
public class SpringTaskDemo {

    private static final Logger log = LoggerFactory.getLogger(SpringTaskDemo.class);

    /**
     * @Async 代表该任务可以进行异步工作，由原本的串行改成并行
     * @throws InterruptedException
     */
    @Async
    @Scheduled(cron = "0/1 * * * * *")
    public void scheduled1() throws InterruptedException {
        Thread.sleep(3000);
        log.info("schedule1 每1秒执行一次：{}", LocalDateTime.now());
    }

    /**
     * fixedRate: 每隔多久执行一次，无视工作时间（@Scheduled(fixedRate = 2000)）
     * @throws InterruptedException
     */
    @Scheduled(fixedRate = 1000)
    public void scheduled2() throws InterruptedException {
        Thread.sleep(3000);
        log.info("schedule2 每1秒执行一次：{}", LocalDateTime.now());
    }

    /**
     * fixedDelay： 当前任务执行完毕之后等待多久继续下次任务（@Scheduled(fixedDelay = 3000)）
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 3000)
    public void scheduled3() throws InterruptedException {
        Thread.sleep(5000);
        log.info("scheduled3 上次任务执行完毕之后隔3秒继续执行：{}", LocalDateTime.now());
    }
}
