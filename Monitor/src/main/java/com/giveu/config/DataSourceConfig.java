package com.giveu.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * 数据源配置类
 * Created by fox on 2018/7/27.
 */
@Configuration
@PropertySource(value = { "classpath:jdbc.properties" }, ignoreResourceNotFound = true)
public class DataSourceConfig {

	/*********************************Mysql****************************************/
	@Value("${mysql.jdbc.url}")
	private String jdbcUrlMysql;

	@Value("${mysql.jdbc.driverClassName}")
	private String jdbcDriverClassNameMysql;

	@Value("${mysql.jdbc.username}")
	private String jdbcUsernameMysql;

	@Value("${mysql.jdbc.password}")
	private String jdbcPasswordMysql;


	/*********************************Oracle****************************************/
	@Value("${oracle.jdbc.url}")
	private String jdbcUrlOracle;

	@Value("${oracle.jdbc.driverClassName}")
	private String jdbcDriverClassNameOracle;

	@Value("${oracle.jdbc.username}")
	private String jdbcUsernameOracle;

	@Value("${oracle.jdbc.password}")
	private String jdbcPasswordOracle;


	/**
	 * 创建 mysql 的数据源
	 * @return
	 */
	@Bean(name = "mysqlDataSource")
	public DataSource mysqlDataSource() {
		return getDataSource(jdbcDriverClassNameMysql, jdbcUrlMysql, jdbcUsernameMysql, jdbcPasswordMysql);
	}


	/**
	 * 创建 oracle 的数据源
	 * @return
	 */
	@Bean(name = "oracleDataSource")
	public DataSource oracleDataSource() {
		return getDataSource(jdbcDriverClassNameOracle, jdbcUrlOracle, jdbcUsernameOracle, jdbcPasswordOracle);
	}

	/**
	 * 获得数据源共用方法
	 * @param driverName
	 * @param jdbcUrl
	 * @param username
	 * @param password
	 * @return
	 */
	public BoneCPDataSource getDataSource(String driverName, String jdbcUrl, String username, String password) {
		BoneCPDataSource boneCPDataSource = new BoneCPDataSource();
		// 数据库驱动
		boneCPDataSource.setDriverClass(driverName);
		// 相应驱动的jdbcUrl
		boneCPDataSource.setJdbcUrl(jdbcUrl);
		// 数据库的用户名
		boneCPDataSource.setUsername(username);
		// 数据库的密码
		boneCPDataSource.setPassword(password);
		// 检查数据库连接池中空闲连接的间隔时间，单位是分，默认值：240，如果要取消则设置为0
		boneCPDataSource.setIdleConnectionTestPeriodInMinutes(60);
		// 连接池中未使用的链接最大存活时间，单位是分，默认值：60，如果要永远存活设置为0
		boneCPDataSource.setIdleMaxAgeInMinutes(30);
		// 每个分区最大的连接数
		boneCPDataSource.setMaxConnectionsPerPartition(3);
		// 每个分区最小的连接数
		boneCPDataSource.setMinConnectionsPerPartition(1);
		return boneCPDataSource;
	}
}
