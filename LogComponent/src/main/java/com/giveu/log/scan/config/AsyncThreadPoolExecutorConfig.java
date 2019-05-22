package com.giveu.log.scan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by fox on 2018/8/16.
 */
@Configuration
//@EnableAspectJAutoProxy
@EnableAsync
public class AsyncThreadPoolExecutorConfig {

	@Bean(name = "executor")
	public Executor getThreadPoolExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50);
		executor.setMaxPoolSize(150);
		executor.setQueueCapacity(50000);
		executor.setThreadNamePrefix("executor-");
		executor.initialize();
		return executor;
	}
}
