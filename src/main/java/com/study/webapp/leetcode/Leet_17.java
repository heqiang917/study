package com.study.webapp.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 电话号码数字字母组合
 * 
 * @author admin
 *
 *         2018年10月30日
 */
public class Leet_17 {

	public static void main(String[] args) {
		System.out.println(JSON.toJSON(letterCombinations("234")));
	}

	public List<String> letterCombinations2(String digits) {
		List<String> list = new ArrayList<>();
		Map<Integer, String[]> map = new HashMap<>();
		map.put(2, new String[] { "a", "b", "c" });
		map.put(3, new String[] { "d", "e", "f" });
		map.put(4, new String[] { "g", "h", "i" });
		map.put(5, new String[] { "j", "k", "l" });
		map.put(6, new String[] { "m", "n", "o" });
		map.put(7, new String[] { "p", "q", "r", "s" });
		map.put(8, new String[] { "t", "u", "v" });
		map.put(9, new String[] { "w", "x", "y", "z" });
		String[] str = map.get(digits.charAt(digits.length() - 1));

		return null;
	}

	/**
	 * https://blog.csdn.net/hit1110310422/article/details/80892911
	 * 
	 * @param digits
	 * @return
	 */
	public static List<String> letterCombinations(String digits) {
		String[][] numberList = new String[][] { { "a", "b", "c" }, { "d", "e", "f" }, { "g", "h", "i" },
				{ "j", "k", "l" }, { "m", "n", "o" }, { "p", "q", "r", "s" }, { "t", "u", "v" },
				{ "w", "x", "y", "z" }, };
		List<String> res = new ArrayList<>();
		if (digits.length() == 0) {
			return res;
		}
		int first = digits.charAt(0) - 48 - 2;
		String[] current = numberList[first];
		if (digits.length() == 1) {
			res.addAll(Arrays.asList(current));
			return res;
		}

		List<String> leftList = letterCombinations(digits.substring(1));

		for (String aCurrent : current) {
			for (String str : leftList) {
				res.add(aCurrent + str);
			}
		}
		return res;
	}

}
