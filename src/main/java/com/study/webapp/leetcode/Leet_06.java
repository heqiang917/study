package com.study.webapp.leetcode;

import java.util.ArrayList;
import java.util.List;

public class Leet_06 {

	public static void main(String[] args) {
		String Str = "PAYPALISHIRING";
		int c = 4;
		System.out.println(method2(Str, c));
	}

	/**
	 * 循环原字符串，根据每一个字符串存放新的位置来
	 * 
	 * @param s
	 * @param numRows
	 * @return
	 */
	public static String method2(String s, int numRows) {

		if (numRows == 1) {
			return s;
		}
		// 按照划分的行数来new字符串，一行一个字符串
		List<StringBuilder> rows = new ArrayList<>();
		for (int i = 0; i < Math.min(numRows, s.length()); i++) {
			rows.add(new StringBuilder());
		}
		int curRow = 0;
		boolean goingDown = false;

		for (char c : s.toCharArray()) {
			rows.get(curRow).append(c);
			if (curRow == 0 || curRow == numRows - 1) {
				goingDown = !goingDown;
			}
			curRow += goingDown ? 1 : -1;
		}

		StringBuilder ret = new StringBuilder();
		for (StringBuilder row : rows) {
			ret.append(row);
		}
		return ret.toString();
	}

	public static String method1(String s, int numRows) {

		if (numRows == 1) {
			return s;
		}
		StringBuilder ret = new StringBuilder();
		int n = s.length();
		int cycleLen = 2 * numRows - 2;

		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j + i < n; j += cycleLen) {
				ret.append(s.charAt(j + i));
				if (i != 0 && i != numRows - 1 && j + cycleLen - i < n)
					ret.append(s.charAt(j + cycleLen - i));
			}
		}
		return ret.toString();
	}

}
