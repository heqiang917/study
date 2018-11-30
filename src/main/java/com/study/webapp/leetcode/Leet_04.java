package com.study.webapp.leetcode;

/**
 * 未完成 https://blog.csdn.net/qq_41014682/article/details/79812181
 * 
 * @author admin
 *
 *         2018年10月22日
 */
public class Leet_04 {

	public double findMedianSortedArrays(int[] A, int[] B) {
		int m = A.length;
		int n = B.length;
		if (m > n) {
			int[] temp = A;
			A = B;
			B = temp;
			int tmp = m;
			m = n;
			n = tmp;
		}
		int iMin = 0;
		int iMax = m;
		int halfLen = (m + n + 1) / 2;
		while (iMin <= iMax) {
			int i = (iMin + iMax) / 2;
			int j = halfLen - i;
			if (i < iMax && B[j - 1] > A[i]) {
				iMin = iMin + 1;
			}
		}
		return 0.0;
	}

}
