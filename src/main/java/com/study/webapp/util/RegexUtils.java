package com.study.webapp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具
 * @author xwb
 *
 */
public final class RegexUtils {
	
	/**
	 * 提取首個匹配的正則字符
	 * @param regex
	 * @param source
	 * @return
	 */
	public static String getMatcher(String regex, String source) {  
        String result = "";  
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(source);  
        while (matcher.find()) {  
            result = matcher.group(1);//只取第一组  
        }  
        return result;  
    }
	
	/**
	 * 提取所有匹配的正則字符
	 * @param regex
	 * @param source
	 * @return
	 */
	public static List<String> getMatcherList(String regex, String source) {  
		List<String> result = new ArrayList<String>(0);  
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(source);  
        
        while (matcher.find()) {
        	int count=matcher.groupCount();
        	for(int i=1;i<=count;i++){
//        		System.out.println(matcher.group(i));
        		result.add(matcher.group(i));
        	}
        }
        return result;  
    }
	
	
}
