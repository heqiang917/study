package com.study.webapp.leetcode;

public class Leet_09 {

	public static void main(String[] args) {
		System.out.println(method(121));
	}

	public static boolean method(int a) {
		String aStr = String.valueOf(a);
		if (aStr.charAt(0) == '-') {
			return false;
		}
		if (aStr.length() > 1) {
			if (aStr.charAt(aStr.length() - 1) == '0') {
				return false;
			}
		}
		char[] ch = aStr.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = ch.length - 1; i >= 0; i--) {
			sb.append(ch[i]);
		}
		String bStr = sb.toString();
		if (aStr.equals(bStr)) {
			return true;
		} else {
			return false;
		}
	}

}
