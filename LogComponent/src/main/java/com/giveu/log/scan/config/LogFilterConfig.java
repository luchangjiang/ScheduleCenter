package com.giveu.log.scan.config;

import com.giveu.log.scan.component.RepeatedlyReadFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by fox on 2018/11/30.
 */
@Configuration
public class LogFilterConfig {

	@Bean
	public FilterRegistrationBean repeatedlyReadFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		RepeatedlyReadFilter repeatedlyReadFilter = new RepeatedlyReadFilter();
		registration.setFilter(repeatedlyReadFilter);
		registration.addUrlPatterns("/*");
		return registration;
	}
}
