package com.giveu.config;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Mybatis 配置基类
 * Created by fox on 2018/8/2.
 */
public class MybatisBaseConfig {

	private static final String TYPEALIASESPACKAGE = "com.giveu.entity";

	SqlSessionFactory getSqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		// 设置数据源
		sqlSessionFactoryBean.setDataSource(dataSource);
		Configuration configuration = new Configuration();
		// 设置驼峰式转换
		configuration.setMapUnderscoreToCamelCase(true);
		sqlSessionFactoryBean.setConfiguration(configuration);
		// 设置别名包
		sqlSessionFactoryBean.setTypeAliasesPackage(TYPEALIASESPACKAGE);
		// 设置mapper文件
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}
}
