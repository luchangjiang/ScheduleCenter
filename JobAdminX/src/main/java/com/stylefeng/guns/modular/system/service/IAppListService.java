package com.stylefeng.guns.modular.system.service;

import com.giveu.job.common.vo.AppVo;
import com.giveu.job.common.vo.ResultModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 应用列表Service
 *
 * @author fengshuonan
 * @Date 2018-07-03 17:28:13
 */
public interface IAppListService {


	void list(HttpServletRequest request, List<Map<String, Object>> list) throws IOException;

	ResultModel list();

	String getListJson();

	String getListJsonByAccount();

	ResultModel addApp(AppVo appVo);

	Integer updAppById(AppVo appVo) throws IOException;

	AppVo getAppVoById(String id) throws IOException;

	Integer delAppById(String id);
	Integer enableById(String id);
	Integer disableById(String id);

	List<String> getAccountList();

	String getBindAccountList(String appKey);

	void bindAccount(String accountListJson, String appKey);



}
