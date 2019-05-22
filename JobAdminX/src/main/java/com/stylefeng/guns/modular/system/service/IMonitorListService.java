package com.stylefeng.guns.modular.system.service;

import com.giveu.job.common.vo.MonitorObjectVo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 监控列表Service
 *
 * @author fengshuonan
 * @Date 2018-07-23 10:18:15
 */
public interface IMonitorListService {

	void list(HttpServletRequest request, List<Map<String, Object>> list) throws IOException;

	MonitorObjectVo getMonitorObjectVoById(String id) throws IOException;

	int updObjPolicySettings(String id, String objPolicySettings);

}
