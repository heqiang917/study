package com.study.webapp.extend;

import java.util.Random;

public class Generator {

	private Random rand = new Random(47);

	public Shap next() {
		switch (rand.nextInt(3)) {
		case 0:
			return new Circle();
		case 1:
			return new Square();
		default:
			return new Shap();
		}
	}

}
