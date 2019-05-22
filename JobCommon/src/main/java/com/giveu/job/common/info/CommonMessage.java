package com.giveu.job.common.info;

/**
 * Created by fox on 2018/6/30.
 */
public class CommonMessage {

	public static final Integer OK_CODE = 200;
	public static final String OK_DESC = "SUCCESS";

	public static final Integer ERROR_CODE = 500;
	public static final String ERROR_DESC = "ERROR";

	public static final Integer PAR_NOT_NULL_CODE = 511;
	public static final String PAR_NOT_NULL_DESC = "参数不能为空";

	public static final Integer PAR_TYPE_ERROR_CODE = 512;
	public static final String PAR_TYPE_ERROR_DESC = "参数类型不对";

	public static final Integer REQ_RES_ERROR_CODE = 513;
	public static final String REQ_RES_ERROR_DESC = "不存在请求资源";

	public static final Integer SER_UNKNOW_ERROR_CODE = 514;
	public static final String SER_UNKNOW_ERROR_DESC = "出现未知错误";

	public static final Integer TABLE_NOT_FOUND_ERROR_CODE = 515;
	public static final String TABLE_NOT_FOUND_ERROR_DESC = "表中无数据";

	public static final Integer JOB_EXE_ERROR_CODE = 516;
	public static final String JOB_EXE_ERROR_DESC = "执行错误";

	public static final Integer CRON_EXPRESSION_ERROR_CODE = 517;
	public static final String CRON_EXPRESSION_ERROR_DESC = "CRON表达式格式错误";

	public static final Integer JOB_NAME_ALREADY_ERROR_CODE = 518;
	public static final String JOB_NAME_ALREADY_ERROR_DESC = "JOBNAME已存在";

	public static final Integer APP_KEY_ALREADY_ERROR_CODE = 519;
	public static final String APP_KEY_ALREADY_ERROR_DESC = "APPKEY已存在";

	public static final Integer SIGN_ERROR_CODE = 520;
	public static final String SIGN_ERROR_DESC = "签名错误";

	public static final Integer NO_AUTHORITY_ERROR_CODE = 521;
	public static final String NO_AUTHORITY_ERROR_DESC = "没有权限";


}
