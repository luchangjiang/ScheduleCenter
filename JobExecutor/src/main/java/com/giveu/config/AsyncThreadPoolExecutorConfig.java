package com.giveu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by fox on 2018/8/16.
 */
@Configuration
@EnableAsync
public class AsyncThreadPoolExecutorConfig {

	@Bean(name = "executor")
	public Executor getThreadPoolExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50);
		executor.setMaxPoolSize(100);
		executor.setQueueCapacity(5000);
		executor.setThreadNamePrefix("executor-");
		executor.initialize();
		return executor;
	}
}
