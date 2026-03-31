package com.example.urlshortner.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;

@Slf4j
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor(){

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");

        executor.setRejectedExecutionHandler(customRejectionHandler());

        executor.initialize();
        return executor;
    }

    private RejectedExecutionHandler customRejectionHandler(){
        return (runnable, executor) -> {
            log.warn("Async task rejected due to high load. Dropping task.");
        };
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
        return (ex, method, params) -> {
            log.error("Async error in method: {}", method.getName(), ex);
        };
    }
}
