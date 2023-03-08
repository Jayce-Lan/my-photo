package com.photo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 定时任务的线程池
 */
public class SchedulerConfig {
    /**
     * 设置执行线程池为3，最大线程数为10
     * @return
     */
    @Bean
    public Executor taskScheduler() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(3);
        executor.initialize();
        return executor;
    }
}
