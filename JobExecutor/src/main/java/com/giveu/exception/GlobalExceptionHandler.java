package com.giveu.exception;

import com.giveu.job.common.info.CommonMessage;
import com.giveu.job.common.vo.ResultModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fox on 2018/11/30.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	//500的异常会被这个方法捕获
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResultModel handleError(HttpServletRequest req, HttpServletResponse rsp, Exception e) throws Exception {
		ResultModel resultModel = new ResultModel();
		resultModel.setCode(CommonMessage.ERROR_CODE);
		resultModel.setMessage("Error! Contact the developer Fox as soon as possible.");
		logger.error(ExceptionUtils.getStackTrace(e));
		return resultModel;
	}


}