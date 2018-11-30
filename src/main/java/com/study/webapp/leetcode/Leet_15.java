package com.study.webapp.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 三数之和
 * 
 * @author admin
 *
 *         2018年10月30日
 */
public class Leet_15 {

	public static void main(String[] args) {
		int[] nums = { -1, 0, 1, 2, -1, -4 };
		System.out.println(JSON.toJSON(threeSum(nums)));
	}

	public static List<List<Integer>> threeSum(int[] nums) {
		List<List<Integer>> listAll = new ArrayList<>();
		if (nums.length < 3) {
			return listAll;
		}
		Arrays.sort(nums);
		for (int i = 0; i < nums.length; i++) {
			int j = i + 1;
			int k = nums.length - 1;
			while (j < k) {
				int sum = nums[i] + nums[j] + nums[k];
				if (sum == 0) {
					List<Integer> list = new ArrayList<>();
					list.add(nums[i]);
					list.add(nums[j]);
					list.add(nums[k]);
					if (!listAll.contains(list)) {
						listAll.add(list);
					}
					j++;
					k--;
				} else if (sum < 0) {
					j++;
				} else {
					k--;
				}
			}
		}
		return listAll;
	}
}
