package com.giveu.config;

import com.giveu.component.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * handle拦截器配置
 * Created by fox on 2018/8/10.
 */
@Configuration
public class WebConfigureAdapter extends WebMvcConfigurationSupport {
	@Autowired
	private Interceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor).addPathPatterns("/**/handle");
	}
}
