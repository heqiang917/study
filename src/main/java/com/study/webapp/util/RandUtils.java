/**
 * 
 */
package com.study.webapp.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomUtils;


/**
 * 随机码生成器
 * 
 * @author xwb
 * 
 */
public class RandUtils {

	/**
	 * 随机生成指定位数的字符串
	 * 
	 * @param numberFlag
	 * @param length
	 * @return
	 */
	public static String createRandomCode(boolean numberFlag, int length) {
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);

		return retStr;
	}
	
	/**
	 * 随机日期 （排除凌晨1点到5点）
	 * 
	 * @param numberFlag
	 * @param length
	 * @return
	 */
	public static long randTimeMilsExNeight(long minTime, long maxTime) {
		if(minTime>maxTime){
			throw new RuntimeException("Start value must be smaller or equal to end value.");
		}
		long date_long=RandomUtils.nextLong(minTime, maxTime);
		Date date =new Date(date_long);
		Calendar calender = Calendar.getInstance();
		calender.setTimeInMillis(date_long);
		int hour=calender.get(Calendar.HOUR_OF_DAY);
		if(hour>=1&&hour<=5){
			calender.set(Calendar.HOUR_OF_DAY, RandomUtils.nextInt(6,23));
		}
		return calender.getTimeInMillis();
	}
	
	/**
	 * 随机 [min-max] 整数 
	 * 
	 * @param numberFlag
	 * @param length
	 * @return
	 */
	public static Integer randomInt(Integer min, Integer max) {
		int rand=0;
		Random random = new Random();
		rand=random.nextInt(max-min+1)+min;
		return rand;
	}
}
