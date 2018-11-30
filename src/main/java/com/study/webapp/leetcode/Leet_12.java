package com.study.webapp.leetcode;

/**
 * 整数转罗马数字 ：https://blog.csdn.net/hit1110310422/article/details/80808382
 * 
 * @author admin
 *
 *         2018年10月26日
 */
public class Leet_12 {
	/**
	 * 题意很简单,最基本的有1,5,10,50,100,500,1000.其他的需要特殊处理的有4,,9,49,90,400,900.
	 * 所以建立映射关系{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4,
	 * 1},依次从大到小检查即可.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(intToRoman(54));
	}

	public static String intToRoman(int num) {
		if (num <= 0)
			return "";
		StringBuilder ret = new StringBuilder();
		int number[] = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
		String flags[] = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
		for (int i = 0; i < 13 && num > 0; i++) {
			if (num < number[i]) {
				continue;
			}
			while (num >= number[i]) {
				num -= number[i];
				ret.append(flags[i]);
			}
		}
		return ret.toString();
	}

}
