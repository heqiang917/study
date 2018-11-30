package com.study.webapp.extend;

public class DotNew {

	static class Inner {
		public static void f() {
			System.out.println("aaa");
		}
	}

	public static void main(String[] args) {
		DotNew dn = new DotNew();
		DotNew.Inner.f();
	}

}
