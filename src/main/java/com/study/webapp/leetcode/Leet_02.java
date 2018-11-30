package com.study.webapp.leetcode;

import java.util.LinkedList;

/**
 * 无重复字符的最长子串
 * 
 * @author admin
 *
 *         2018年10月19日
 */
public class Leet_02 {

	public static void main(String[] args) {
		String langStr = "uqinntq";
		System.out.println(lengStr(langStr));

	}

	public static int lengStr(String s) {
		int num = 0;
		int sum = 0;
		char[] arr = s.toCharArray();
		LinkedList<Character> temp = new LinkedList<>();
		for (int i = 0; i < arr.length; i++) {
			if (!temp.contains(arr[i])) {
				temp.add(arr[i]);
				num = temp.size();
				if (num > sum) {
					sum = num;
				}
			} else {
				int first = temp.indexOf(arr[i]);
				for (int j = 0; j < first + 1; j++) {
					temp.remove();
				}
				temp.add(arr[i]);
			}
		}
		return sum;
	}

}
