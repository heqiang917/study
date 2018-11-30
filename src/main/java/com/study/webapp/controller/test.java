package com.study.webapp.controller;

import java.util.Random;

public class test {

	static Random r = new Random(47);

	private static final int a = r.nextInt();

	private static final int b = r.nextInt(20);

	public static void main(String[] args) {

		System.out.println(a);
		System.out.println(b);
	}

}
