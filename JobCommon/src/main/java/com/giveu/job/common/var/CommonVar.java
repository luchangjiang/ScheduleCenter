package com.giveu.job.common.var;

/**
 * 公共变量
 * Created by fox on 2018/6/29.
 */
public class CommonVar {
	// JOB和TRIGGER的默认组名
	public static final String JOB_GROUP = "GiveuGroup";

	// 调度执行器的服务的默认实例名称
	public static final String JOB_EXECUTOR = "JOB-EXECUTOR";

	// 回调测试地址
	public static final String JOB_TEST_APP_URL = "http://localhost:8080/job";

	// 实例健康检测轮训周期时间（MS）
	public static final Integer EXECUTOR_HEALTH_PERIOD_TIME_MS = 3000;

	// 日志表名前缀
	public static final String LOG_TABLE_NAME_PREFIX = "QRTZ_TRIGGER_LOG_";

	// 管理端APPKEY
	public static final String ADMIN_APP_KEY = "admin";
}
