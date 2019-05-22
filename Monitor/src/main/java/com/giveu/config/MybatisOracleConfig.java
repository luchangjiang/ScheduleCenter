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
@MapperScan(basePackages = {"com.giveu.dao.oracle"}, sqlSessionFactoryRef = "oracleSqlSessionFactory", sqlSessionTemplateRef = "oracleSqlSessionTemplate")
public class MybatisOracleConfig extends MybatisBaseConfig{

	@Autowired
	@Qualifier("oracleDataSource")
	DataSource oracleDataSource;

	@Bean(name = "oracleSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		return getSqlSessionFactory(oracleDataSource);
	}

	@Bean(name = "oracleTransactionManager")
	public PlatformTransactionManager primaryTransactionManager(@Qualifier("oracleDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "oracleSqlSessionTemplate")
	public SqlSessionTemplate oracleSqlSessionTemplate(@Qualifier("oracleSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
