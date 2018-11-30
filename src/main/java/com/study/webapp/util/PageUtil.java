package com.study.webapp.util;

/**
 * Created by rocky on 2017/3/29. 分页工具类
 */
public class PageUtil {

	/**
	 * 获取分页起始位置
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	public static int getOffset(int page, int size) {
		int offset = (page - 1) * size;
		if (offset < 0) {
			offset = 0;
		}
		return offset;
	}
}
