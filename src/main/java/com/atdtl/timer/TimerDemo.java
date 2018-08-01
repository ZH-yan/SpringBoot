package com.atdtl.timer;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  定时任务-实现方式
 *      1.Timer JDK自带的 java.uti.Timer ; 通过时间调度java.util.TimerTask 的方式让程序按某一个频度执行
 *      但不能在指定时间运行。一般用的较少。
 *      2.ScheduledExecutorService： JDK1.5新增的，位于java.util.concurrent 包中；是基于线程池设计的定时任务类，
 *      每个调度任务都会被分到线程池中，并发执行，互不影响
 *      3.Spring Task Spring3.0以后新增了task，一个轻量级的Quartz，功能够用，用法简单。
 *      4.Quartz：功能最强大的调度器，可以让程序在指定时间执行，也可以按照某一频度执行，还可以动态开发，但配置复杂。
 *
 * @author Administrator
 * @since 2018/7/26 8:45
 */
public class TimerDemo {

    public static void main(String[] args) {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("执行任务：" + LocalDateTime.now());
            }
        };

        Timer timer = new Timer();

        /**
         * timertask: 需要执行的任务
         * delay: 延迟时间（以毫秒为单位）
         * period: 间隔时间（以毫秒为单位）
         */
        timer.schedule(timerTask, 5000, 3000);
    }
}
