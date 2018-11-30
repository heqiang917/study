package com.study.webapp.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 给定一个包含 n 个整数的数组 nums 和一个目标值 target， 判断 nums 中是否存在四个元素 a，b，c 和 d ，使得 a + b + c
 * + d 的值与 target 相等？ 找出所有满足条件且不重复的四元组。
 * https://blog.csdn.net/hit1110310422/article/details/80934545
 * 
 * @author admin
 *
 *         2018年11月29日
 */
public class Leet_18 {

	public static void main(String[] args) {
		int[] nums = { 1, 0, -1, 0, -2, 2 };
		List<List<Integer>> list = fourSum(nums, 0);
		System.out.println(JSON.toJSONString(list));
	}

	public static List<List<Integer>> fourSum(int[] nums, int target) {
		List<List<Integer>> list = new ArrayList<>();
		if (nums.length < 4) {
			return null;
		}
		Arrays.sort(nums);
		for (int i = 0; i < nums.length; i++) {
			if (i > 0 && nums[i] == nums[i - 1]) {
				continue;
			}
			for (int j = i + 1; j < nums.length; j++) {
				if (j > 1 + 1 && nums[j] == nums[j - 1]) {
					continue;
				}
				int l = j + 1;
				int r = nums.length - 1;
				while (l < r) {
					int sum = nums[i] + nums[j] + nums[l] + nums[r];
					if (sum == target) {
						list.add(Arrays.asList(nums[i], nums[j], nums[l], nums[r]));
						while (l < r && nums[l] == nums[l + 1]) {
							l++;
						}
						while (l < r && nums[r] == nums[r - 1]) {
							r--;
						}
						l++;
						r--;
					} else if (sum < target) {
						l++;
					} else {
						r--;
					}
				}
			}
		}

		return list;
	}

}
