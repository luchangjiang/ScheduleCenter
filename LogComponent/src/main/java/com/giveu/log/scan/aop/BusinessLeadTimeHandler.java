package com.giveu.log.scan.aop;

import com.giveu.log.scan.common.ThreadLocalCommon;
import com.giveu.log.scan.vo.MonitorLog;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by fox on 2018/8/8.
 */
//@Order(100)
@Aspect
@Component
public class BusinessLeadTimeHandler {

	public static Logger logger = LoggerFactory.getLogger(BusinessLeadTimeHandler.class);

	@Around("execution(* com..*.*Service.*(..))")
//	@Around("(execution(public * com.giveu.*.service.impl.*Impl.*(..))) && !(execution(public * com.giveu.common.service.impl.*Impl.*(..)))")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MonitorLog monitorLog = ThreadLocalCommon.threadLocalMonitorLog.get();



		try {
			long startTime = System.currentTimeMillis();
			Object obj = proceedingJoinPoint.proceed();
			long endTime = System.currentTimeMillis();
			Long leadTime = endTime - startTime;
//			logger.debug("Business Lead time： " + leadTime);
			if (monitorLog != null) {
				monitorLog.setBusinessMilliseconds(leadTime);
			}
			return obj;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			monitorLog.setExceptionTraceStack(ExceptionUtils.getStackTrace(throwable));
			throw throwable;
		}
	}
}
