package com.study.webapp.po;

public class Facts {

	private final int servingSize;
	private final int servings;
	private final int calorise;
	private final int fat;
	private final int sodium;
	private final int car;

	public static class Buider {
		private final int servingSize;
		private final int servings;
		private int calorise = 0;
		private int fat = 0;
		private int sodium = 0;
		private int car = 0;

		public Buider(int servingSize, int servings) {
			this.servingSize = servingSize;
			this.servings = servings;
		}

		public Buider calories(int val) {
			calorise = val;
			return this;
		}

		public Buider fat(int val) {
			fat = val;
			return this;
		}

		public Buider sodium(int val) {
			sodium = val;
			return this;
		}

		public Buider car(int val) {
			car = val;
			return this;
		}

		public Facts buid() {
			return new Facts(this);
		}
	}

	private Facts(Buider buider) {
		servingSize = buider.servingSize;
		servings = buider.servings;
		calorise = buider.calorise;
		fat = buider.fat;
		sodium = buider.sodium;
		car = buider.car;
	}

}
