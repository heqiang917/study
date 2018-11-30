package com.study.webapp.test;

public class MallardDuck implements Quackable {

	Observable observable;

	public MallardDuck() {
		observable = new Observable(this);
	}

	@Override
	public void quack() {
		System.out.println("Quack");
		notifyObserver();
	}

	@Override
	public void registerObserver(Observer observer) {
		observable.registerObserver(observer);

	}

	@Override
	public void notifyObserver() {
		observable.notifyObserver();

	}

}
