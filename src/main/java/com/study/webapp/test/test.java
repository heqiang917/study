package com.study.webapp.test;

import org.apache.commons.lang3.StringUtils;

public class test {

	public static void main2(String[] args) {
		String aa = "0380[会审]苏州创易技研股份有限公司5.xls";
		String bianzhi = StringUtils.substringAfterLast(aa, "]");
		String fileType = StringUtils.substringBeforeLast(bianzhi, "司") + "司";
		System.out.println(bianzhi);
		System.out.println(fileType);
	}

	public static void main(String[] args) {
		String a = "风险准备";
		int b = 1234567;
		Integer c = 1234567;
		System.out.println(a.indexOf("风险准备"));
	}

}
