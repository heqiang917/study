package com.study.webapp.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * https://blog.csdn.net/hit1110310422/article/details/80957087
 * 
 * @author admin
 *
 *         2018年11月30日
 */
public class Leet_20 {

	public static void main(String[] args) {
		String s = "{}[]()";
		System.out.println(isBoolean(s));
	}

	public static boolean isBoolean(String s) {
		Map<Character, Character> map = new HashMap<>();
		map.put(')', '(');
		map.put('}', '{');
		map.put(']', '[');
		char[] stack = new char[s.length()];
		int top = -1;
		for (int i = 0; i < s.length(); i++) {
			if (map.containsValue(s.charAt(i))) {
				stack[++top] = s.charAt(i);
			}
			if (map.containsKey(s.charAt(i))) {
				if (top == -1 || stack[top--] == map.get(s.charAt(i))) {
					return false;
				}
			}
		}
		return top == -1;
	}

}
