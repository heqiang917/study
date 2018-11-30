package com.study.webapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xwb
 *
 */
public class LoggerUtils {

	private static final Logger ACCESS_LOG = LoggerFactory.getLogger("accessLogger");

	private static final Logger RUN_LOG = LoggerFactory.getLogger("runLogger");

	private static final Logger ERROR_LOG = LoggerFactory.getLogger("errorLogger");

	public static final void errorlog(String msg) {
		ERROR_LOG.error(msg);
	}

	public static final void errorlog(String msg, Throwable t) {
		ERROR_LOG.error(msg, t);
	}
	

	public static final void errorlog(Throwable t) {
		ERROR_LOG.error(t.getMessage(),t);
	}
	
	
	public static final void runlog(String msg) {
		RUN_LOG.info(msg);
	}
	
	public static final void runlog(Object obj) {
		RUN_LOG.info(JsonUtils.toJson(obj));
	}

	public static final void accessLog(String msg) {
		ACCESS_LOG.info(msg);
	}
}
