package com.study.webapp.test;

public class DuckSimulator {

	public static void main(String[] args) {
		DuckSimulator simulator = new DuckSimulator();
		AbstractDuckFactory duckFactory = new CountingDuckFactory();
		simulator.simulate(duckFactory);
	}

	// void simulate(AbstractDuckFactory duckFactory) {
	// // Quackable mallardDuck = new QuackCounter(new MallardDuck());
	// // Quackable redheadDuck = new QuackCounter(new RedheadDuck());
	// // Quackable rubberDuck = new QuackCounter(new RubberDuck());
	// // Quackable duckCall = new QuackCounter(new DuckCall());
	// Quackable goose = new GooseAdapter(new Goose());
	//
	// Quackable mallardDuck = duckFactory.createMallardDuck();
	// Quackable redheadDuck = duckFactory.createRedheadDuck();
	// Quackable rubberDuck = duckFactory.createRubberDuck();
	// Quackable duckCall = duckFactory.createDuckCall();
	// System.out.println("\nDuck Simlator");
	// simulate(mallardDuck);
	// simulate(redheadDuck);
	// simulate(rubberDuck);
	// simulate(duckCall);
	// simulate(goose);
	// System.out.println(QuackCounter.getQuacks() + "次");
	// }

	void simulate(AbstractDuckFactory duckFactory) {
		Quackable goose = new GooseAdapter(new Goose());
		Quackable redheadDuck = duckFactory.createRedheadDuck();
		Quackable rubberDuck = duckFactory.createRubberDuck();
		Quackable duckCall = duckFactory.createDuckCall();
		System.out.println("\nDuck Simlator");
		Flock flockDucks = new Flock();
		flockDucks.add(redheadDuck);
		flockDucks.add(duckCall);
		flockDucks.add(rubberDuck);
		flockDucks.add(goose);
		Flock flockMall = new Flock();
		Quackable mallardDuck1 = duckFactory.createMallardDuck();
		Quackable mallardDuck2 = duckFactory.createMallardDuck();
		Quackable mallardDuck3 = duckFactory.createMallardDuck();
		Quackable mallardDuck4 = duckFactory.createMallardDuck();
		flockMall.add(mallardDuck1);
		flockMall.add(mallardDuck2);
		flockMall.add(mallardDuck3);
		flockMall.add(mallardDuck4);
		flockDucks.add(flockMall);
		System.out.println("测试一整群");
		simulate(flockDucks);
		System.out.println("测试绿头鸭群");
		simulate(flockMall);
		System.out.println(QuackCounter.getQuacks() + "次");
	}

	void simulate(Quackable duck) {
		duck.quack();
	}

}
