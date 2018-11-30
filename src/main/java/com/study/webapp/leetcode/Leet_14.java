package com.study.webapp.leetcode;

/**
 * 字符串组最长公共前缀
 * 
 * @author admin
 *
 *         2018年10月30日
 */
public class Leet_14 {

	public static void main(String[] args) {
		String[] strs = { "aa", "aa" };
		System.out.println(method(strs));
	}

	public static String method(String[] strs) {
		StringBuilder builder = new StringBuilder("");
		int len = 0;
		for (String str : strs) {
			if ("".equals(str)) {
				return "";
			} else {
				if (len == 0) {
					len = str.length();
				} else {
					if (len > str.length()) {
						len = str.length();
					}
				}
			}
		}
		for (int i = 0; i < len; i++) {
			int count = 1;
			char c = '0';
			for (String str : strs) {
				if (str.charAt(i) != c) {
					c = str.charAt(i);
				} else {
					count++;
				}
			}
			if (count == strs.length) {
				builder.append(c);
			} else {
				break;
			}
		}
		return builder.toString();
	}

}
