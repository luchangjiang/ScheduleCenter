package com.giveu.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Mybatis 配置类
 * Created by fox on 2018/7/27.
 */
@Configuration
@MapperScan(basePackages = {"com.giveu.dao.mysql"}, sqlSessionFactoryRef = "mysqlSqlSessionFactory", sqlSessionTemplateRef = "mysqlSqlSessionTemplate")
public class MybatisMysqlConfig extends MybatisBaseConfig{

	@Autowired
	@Qualifier("mysqlDataSource")
	DataSource mysqlDataSource;

	@Bean(name = "mysqlSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		return getSqlSessionFactory(mysqlDataSource);
	}

	@Bean(name = "mysqlTransactionManager")
	public PlatformTransactionManager primaryTransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "mysqlSqlSessionTemplate")
	public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
