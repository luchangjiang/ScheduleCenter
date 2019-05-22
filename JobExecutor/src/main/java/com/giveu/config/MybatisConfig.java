package com.giveu.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Mybatis 配置类
 * Created by fox on 2018/7/27.
 */
@Configuration
@MapperScan(basePackages = {"com.giveu.dao"})
public class MybatisConfig {

	@Bean
	@ConditionalOnMissingBean //当容器里没有指定的Bean的情况下创建该对象
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		// 设置数据源
		sqlSessionFactoryBean.setDataSource(dataSource);
		// 设置mybatis的主配置文件
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
		// 设置XML文件映射目录
//		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
		sqlSessionFactoryBean.setConfigLocation(mybatisConfigXml);
		// 设置别名包
//		sqlSessionFactoryBean.setTypeAliasesPackage("com.giveu.entity");
		return sqlSessionFactoryBean;
	}

}
