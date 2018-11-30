package com.study.webapp.extend;

public class ShapMain {

	private static Generator gen = new Generator();

	public static void main(String[] args) {
		Shap[] s = new Shap[9];
		for (int i = 0; i < s.length; i++) {
			s[i] = gen.next();
		}
		for (Shap sh : s) {
			sh.draw();
		}
	}

}
