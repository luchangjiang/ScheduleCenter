package com.stylefeng.guns.modular.system.service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 监控日志Service
 *
 * @author fengshuonan
 * @Date 2018-07-23 10:54:28
 */
public interface IMonitorLogListService {

	void list(HttpServletRequest request, List<Map<String, Object>> list) throws IOException;

}
