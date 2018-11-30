package com.study.webapp.test;

public class DuckSimulator2 {

	public static void main(String[] args) {
		DuckSimulator2 simulator = new DuckSimulator2();
		AbstractDuckFactory duckFactory = new CountingDuckFactory();
		simulator.simulator(duckFactory);
	}

	void simulator(AbstractDuckFactory duckFactory) {
		System.out.println("Observer开始");
		Quackologist quackologist = new Quackologist();
		Flock flock = new Flock();
		flock.registerObserver(quackologist);
		simulate(flock);
		System.out.println("次数");
	}

	void simulate(Quackable duck) {
		duck.quack();
	}

}
