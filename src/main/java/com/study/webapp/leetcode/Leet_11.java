package com.study.webapp.leetcode;

public class Leet_11 {

	public static void main(String[] args) {
		int[] a = { 1, 8, 6, 2, 5, 4, 8, 3, 7 };
		System.out.println(getMax(a));
	}

	/**
	 * 暴力法
	 * 
	 * @param height
	 * @return
	 */
	public static int getMax(int[] height) {
		int len = height.length;
		int sum = 0;
		int temp = 0;
		for (int i = 0; i < len; i++) {
			for (int j = len - 1; j >= 0; j--) {
				if (j > i) {
					int k = height[i] > height[j] ? height[j] : height[i];
					temp = k * (j - i);
					if (temp > sum) {
						sum = temp;
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 双指针法
	 * 
	 * @param height
	 * @return
	 */
	public static int getMax2(int[] height) {
		int maxarea = 0, l = 0, r = height.length - 1;
		while (l < r) {
			maxarea = Math.max(maxarea, Math.min(height[l], height[r]) * (r - l));
			if (height[l] < height[r])
				l++;
			else
				r--;
		}
		return maxarea;
	}

}
