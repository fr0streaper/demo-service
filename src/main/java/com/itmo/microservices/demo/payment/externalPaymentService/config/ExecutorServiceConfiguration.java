package com.itmo.microservices.demo.payment.externalPaymentService.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class ExecutorServiceConfiguration {

    @Bean(name = "executorService")
    public Executor executorService() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // because of max 3000ms request delay
        executor.setQueueCapacity(100);
        executor.initialize();

        return executor;
    }
}
