package com.study.webapp.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * https://blog.csdn.net/qq_32354501/article/details/80084325
 * 
 * @author admin
 *
 *         2018年10月22日
 */
public class Leet_05 {

	public static void main(String[] args) {
		String s = "aaaa";
		System.out.println(method3(s));
	}

	public static String method3(String s) {
		if (s.length() <= 1) {
			return s;
		}
		List<String> list = new ArrayList<>();
		char[] ch = s.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			for (int j = ch.length - 1; j > 0; j--) {
				if (i != j) {
					if (ch[i] == ch[j]) {
						if (i < j + 1) {
							String sub = s.substring(i, j + 1);
							list.add(sub);
						}
					}
				}
			}
		}
		int c = 0;
		String sub = "";
		if (list.size() == 0) {
			return s.substring(0, 1);
		}
		for (String sStr : list) {
			if (list.size() > 1) {
				if (sStr.length() > c) {
					c = sStr.length();
					sub = sStr;
				}
			} else {
				sub = list.get(0);
			}
		}
		if (sub.length() == s.length()) {
			return s.substring(0, 1);
		}
		return sub;
	}

	/**
	 * 中心扩散法
	 */
	private static int maxLen = 0;
	private static String sub = "";

	public static String method2(String s) {
		for (int i = 0; i < s.length() - 1; i++) {
			find(s, i, i);
			find(s, i, i + 1);
		}
		return sub;
	}

	public static void find(String s, int low, int hight) {
		while (low >= 0 && hight <= s.length() - 1) {
			if (s.charAt(low) == s.charAt(hight)) {
				if (hight - low + 1 > maxLen) {
					maxLen = hight - low + 1;
					sub = s.substring(low, hight + 1);
				}
				low--;
				hight++;
			} else {
				break;
			}
		}
	}

	/**
	 * 暴力法
	 * 
	 * @param args
	 */
	public static List<String> method1(String s) {
		List<String> list = new ArrayList<>();
		for (int i = s.length(); i > 0; i--) {
			for (int j = 0; j <= s.length() - i; j++) {
				String sub = s.substring(j, j + i);
				int count = 0;
				for (int k = 0; k < sub.length() / 2; k++) {
					if (sub.charAt(k) == sub.charAt(sub.length() - k - 1)) {
						count++;
					}
					if (count == sub.length() / 2) {
						list.add(sub);
					}
				}
			}
		}
		return list;
	}

}
