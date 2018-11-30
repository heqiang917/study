package com.study.webapp.leetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Leet_07 {

	public static void main(String[] args) {
		System.out.println(method1(987654321));

	}

	public static int method1(int x) {
		String a = String.valueOf(x);
		StringBuilder sb = new StringBuilder();
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(a);
		char[] b = m.replaceAll("").trim().toCharArray();
		String c = a.replaceAll("([1-9]+[0-9]*|0)(\\.[\\d]+)?", "");
		for (int i = b.length - 1; i >= 0; i--) {
			sb.append(b[i]);
		}
		int rev = Integer.parseInt(c + sb.toString());
		if (rev > Integer.MAX_VALUE || rev < Integer.MIN_VALUE) {
			return 0;
		}
		return rev;
	}

}
